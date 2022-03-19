package com.spring.sharepod.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.entity.Authimgbox;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.repository.AuthimgboxRepository;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.validator.AuthValidator;
import com.spring.sharepod.validator.AuthimgboxValidator;
import com.spring.sharepod.validator.BoardValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3Service {
    private final UserRepository userRepository;
    private final AuthimgboxRepository authimgboxRepository;
    private final BoardRepository boardRepository;
    private final BoardValidator boardValidator;
    private final UserValidator userValidator;
    private final AuthimgboxValidator authimgboxValidator;
    private final AuthValidator authValidator;


    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-Key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //유저 프로필 사진 업로드
    public String upload(UserRegisterRequestDto userRegisterRequestDto, MultipartFile file) throws IOException {


        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        fileName = userRegisterRequestDto.getNickname() + fileName;
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }



    //게시판 사진 3개, 영상 1개 업로드
    public BoardWriteRequestDTO boardupload(BoardWriteRequestDTO boardWriteRequestDTO,
                                            MultipartFile[] imgfiles,
                                            MultipartFile videofile) throws IOException {
        //게시판 작성 validator
        boardValidator.validateBoardWrite(boardWriteRequestDTO, imgfiles, videofile);

        //이미지 3개 처리
        User user = userValidator.ValidByUserId(boardWriteRequestDTO.getUserid());

        String[] giveurl = new String[3];
        for (int i = 0; i < imgfiles.length; i++) {
            String filename = UUID.randomUUID() + "_" + imgfiles[i].getOriginalFilename();
            filename = user.getNickname() + filename;
            s3Client.putObject(new PutObjectRequest(bucket, filename, imgfiles[i].getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            giveurl[i] = s3Client.getUrl(bucket, filename).toString();
        }

        boardWriteRequestDTO.setImgurl1(giveurl[0]);
        boardWriteRequestDTO.setImgurl2(giveurl[1]);
        boardWriteRequestDTO.setImgurl3(giveurl[2]);


        //비디오 처리
        String videoname = UUID.randomUUID() + "_" + videofile.getOriginalFilename();
        videoname = user.getNickname() + videoname;
        System.out.println(videoname);
        //https://sharepod.s3.ap-northeast-2.amazonaws.com/be034d91-2265-4913-8d20-ffdf33d88961_new_profile.png
        s3Client.putObject(new PutObjectRequest(bucket, videoname, videofile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        boardWriteRequestDTO.setVideourl(s3Client.getUrl(bucket, videoname).toString());

        return boardWriteRequestDTO;
    }

    //게시판 사진 3개, 영상 1개 수정
    public BoardPatchRequestDTO boardupdate(Long boardid,
                                            BoardPatchRequestDTO patchRequestDTO,
                                            MultipartFile[] imgfiles,
                                            MultipartFile videofile) throws IOException {
        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardValidator.ValidByBoardId(boardid);

        String FirstBoardImgUrl = board.getImgurl1();
        String SecondBoardImgUrl = board.getImgurl2();
        String ThirdBoardImgUrl = board.getImgurl3();
        String VideoUrl = board.getVideourl();

        System.out.println("Video URL :" + VideoUrl);

        //게시판 작성 validator
        boardValidator.validateBoardUpdate(patchRequestDTO);

        //이미지 3개 처리
        User user = userValidator.ValidByUserId(patchRequestDTO.getUserid());

        String[] giveurl = new String[3];
        for (int i = 0; i < imgfiles.length; i++) {
            if (Objects.equals(imgfiles[i].getOriginalFilename(), "")) {
                if (i == 0) { giveurl[i] = board.getImgurl1(); }
                else if (i == 1) { giveurl[i] = board.getImgurl2(); }
                else { giveurl[i] = board.getImgurl3(); }

            }
            else {
                String modifiedBoardImg = "";
                if(i==0){
                    modifiedBoardImg = FirstBoardImgUrl.substring(FirstBoardImgUrl.lastIndexOf("/")+1);
                }
                else if(i==1){
                    modifiedBoardImg = SecondBoardImgUrl.substring(SecondBoardImgUrl.lastIndexOf("/")+1);
                }
                else{
                    modifiedBoardImg = ThirdBoardImgUrl.substring(ThirdBoardImgUrl.lastIndexOf("/")+1);
                }
                System.out.println("modifiedBoardImg          :" + modifiedBoardImg);

                // 게시글 삭제
                s3Client.deleteObject(new DeleteObjectRequest(bucket,modifiedBoardImg));

                String filename = UUID.randomUUID() + "_" + imgfiles[i].getOriginalFilename();
                filename = user.getNickname() + filename;
                s3Client.putObject(new PutObjectRequest(bucket, filename, imgfiles[i].getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                giveurl[i] = s3Client.getUrl(bucket, filename).toString();
            }

        }

        patchRequestDTO.setImgurl1(giveurl[0]);
        patchRequestDTO.setImgurl2(giveurl[1]);
        patchRequestDTO.setImgurl3(giveurl[2]);


        //비디오파일 있는지 확인
        if(Objects.equals(videofile.getOriginalFilename(), "")){
            patchRequestDTO.setVideourl(board.getVideourl());
        }
        else{

            //기존 이미지 삭제
            System.out.println("지우려는 video url 키 값:       " +VideoUrl.substring(VideoUrl.lastIndexOf("/")+1));

            s3Client.deleteObject(bucket,VideoUrl.substring(VideoUrl.lastIndexOf("/")+1));

            //비디오 처리
            String videoname = UUID.randomUUID() + "_" + videofile.getOriginalFilename();
            videoname = user.getNickname() + videoname;
            System.out.println(videoname);
            //https://sharepod.s3.ap-northeast-2.amazonaws.com/be034d91-2265-4913-8d20-ffdf33d88961_new_profile.png
            s3Client.putObject(new PutObjectRequest(bucket, videoname, videofile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            patchRequestDTO.setVideourl(s3Client.getUrl(bucket, videoname).toString());
        }


        return patchRequestDTO;
    }
    //인증 이미지
    public String authimgboxs3(Long userid, Long authimgboxid, MultipartFile authfile) throws IOException {

        //이미지박스 id가 존재하는지 확인
        Authimgbox authimgbox = authimgboxValidator.ValidAuthImgBoxById(authimgboxid);

        //구매자가 아이디가 일치하는지 확인
        authimgboxValidator.ValidAuthImgBoxIdEqualBuyerId(userid,authimgbox.getAuth().getAuthbuyer().getId());


        //이미지 s3 저장
        String imgname = UUID.randomUUID() + "_" + authfile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucket, imgname, authfile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucket, imgname).toString();
    }

//    //유저 프로필 이미지 변경시
//    public String userprofileimgchange(MultipartFile userimgfile) throws IOException {
//        //추후 S3에서 기존 이미지를 삭제해주는 과정을 해야함
//
//
//        String userimg = UUID.randomUUID() + "_" + userimgfile.getOriginalFilename();
//        s3Client.putObject(new PutObjectRequest(bucket, userimg, userimgfile.getInputStream(), null)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//        return s3Client.getUrl(bucket, userimg).toString();
//    }
}


