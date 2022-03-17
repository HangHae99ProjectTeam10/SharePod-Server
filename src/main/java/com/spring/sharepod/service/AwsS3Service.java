package com.spring.sharepod.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.validator.BoardValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final BoardValidator boardValidator;
    private final UserValidator userValidator;

    // 회원 탈퇴 시, 진행되는 로직
    public void deleteProfileImg(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println("이미지 삭제 완료");
    }

    //회원정보 수정 시, 파일이 바뀌었다면 진행되는 로직
    public String ModifiedProfileImg(String fileName, String userNickName ,MultipartFile userimgfile)throws IOException {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println("기존 이미지 삭제 완료");

        String modifiedFileName = UUID.randomUUID() + "_" + userimgfile.getOriginalFilename();
        modifiedFileName = userNickName + modifiedFileName;

        amazonS3.putObject(new PutObjectRequest(bucket, modifiedFileName, userimgfile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        System.out.println("새로운 이미지 등록 완료");

        return amazonS3.getUrl(bucket,modifiedFileName).toString();
    }


    //파일 여러개 삭제(게시판 삭제)
    public void deleteFile(List<String> fileName) {
        try {
            for (int i=0; i <=fileName.size()-1; i++){
                //s3Client.deleteObject(bucket, (fileName.get(i)).replace(File.separatorChar, '/'));

                System.out.println("1");
                System.out.println("keys" +fileName.get(i));
                DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName.get(i));

                System.out.println("2");
                amazonS3.deleteObject(request);
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }catch (SdkClientException e) {
            e.printStackTrace();
        }

    }



    //게시판 사진 3개, 영상 1개 수정
//    public BoardPatchRequestDTO boardupdate(Long boardid,
//                                            BoardPatchRequestDTO patchRequestDTO,
//                                            MultipartFile[] imgfiles,
//                                            MultipartFile videofile) throws IOException {
//        //수정할 게시판 boardid로 검색해 가져오기
//        Board board = boardValidator.ValidByBoardId(boardid);
//
//        //게시판 작성 validator
//        boardValidator.validateBoardUpdate(patchRequestDTO);
//
//        //이미지 3개 처리
//        User user = userValidator.ValidByUserId(patchRequestDTO.getUserid());
//
//        String[] giveurl = new String[3];
//        for (int i = 0; i < imgfiles.length; i++) {
//            if (Objects.equals(imgfiles[i].getOriginalFilename(), "")) {
//                if (i == 0) { giveurl[i] = board.getImgurl1(); }
//                else if (i == 1) { giveurl[i] = board.getImgurl2(); }
//                else { giveurl[i] = board.getImgurl3(); }
//
//            }
//            else {
//                String ModifiedImgfiles = "";
//                if( i == 0 ) {
//                    ModifiedImgfiles = board.getImgurl1();
//                }
//                else if (i == 1){
//                    ModifiedImgfiles = board.getImgurl2();
//                }
//                else{
//                    ModifiedImgfiles = board.getImgurl3();
//                }
//
//                ModifiedImgfiles = ModifiedImgfiles.substring(ModifiedImgfiles.lastIndexOf("/")+1);
//                System.out.println(ModifiedImgfiles + "             ModifiedImgFiles");
//                amazonS3.deleteObject(new DeleteObjectRequest(bucket, ModifiedImgfiles));
//
//
//                String filename = UUID.randomUUID() + "_" + imgfiles[i].getOriginalFilename();
//                filename = user.getUsername() + filename;
//                amazonS3.putObject(new PutObjectRequest(bucket, filename, imgfiles[i].getInputStream(), null)
//                        .withCannedAcl(CannedAccessControlList.PublicRead));
//                giveurl[i] = amazonS3.getUrl(bucket, filename).toString();
//            }
//
//        }
//
//        patchRequestDTO.setImgurl1(giveurl[0]);
//        patchRequestDTO.setImgurl2(giveurl[1]);
//        patchRequestDTO.setImgurl3(giveurl[2]);
//
//
//        //비디오파일 있는지 확인
//        if(Objects.equals(videofile.getOriginalFilename(), "")){
//            patchRequestDTO.setVideourl(board.getVideourl());
//        }
//        else{
//            String ModifiedVideofile = videofile.getOriginalFilename();
//            ModifiedVideofile = ModifiedVideofile.substring(ModifiedVideofile.lastIndexOf("/")+1);
//            amazonS3.deleteObject(new DeleteObjectRequest(bucket, ModifiedVideofile));
//
//            //비디오 처리
//            String videoname = UUID.randomUUID() + "_" + videofile.getOriginalFilename();
//            System.out.println(videoname);
//            //https://sharepod.s3.ap-northeast-2.amazonaws.com/be034d91-2265-4913-8d20-ffdf33d88961_new_profile.png
//            amazonS3.putObject(new PutObjectRequest(bucket, videoname, videofile.getInputStream(), null)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
//            patchRequestDTO.setVideourl(amazonS3.getUrl(bucket, videoname).toString());
//        }
//
//
//        return patchRequestDTO;
//    }
}
