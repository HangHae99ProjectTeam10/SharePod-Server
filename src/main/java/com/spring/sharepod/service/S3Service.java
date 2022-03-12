package com.spring.sharepod.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.UserRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final UserRepository userRepository;

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
        //이미지 3개 처리
        User user = userRepository.findById(boardWriteRequestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));
        String[] giveurl = new String[3];
        for (int i = 0; i < imgfiles.length; i++) {
            String filename = UUID.randomUUID() + "_" + imgfiles[i].getOriginalFilename();
            filename = user.getUsername() + i + filename;
            s3Client.putObject(new PutObjectRequest(bucket, filename, imgfiles[i].getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            giveurl[i] = s3Client.getUrl(bucket, filename).toString();
        }

        boardWriteRequestDTO.setImgurl1(giveurl[0]);
        boardWriteRequestDTO.setImgurl2(giveurl[1]);
        boardWriteRequestDTO.setImgurl3(giveurl[2]);


        //비디오 처리
        String videoname = UUID.randomUUID() + "_" + videofile.getOriginalFilename();
        System.out.println(videoname);
        //https://sharepod.s3.ap-northeast-2.amazonaws.com/be034d91-2265-4913-8d20-ffdf33d88961_new_profile.png
        s3Client.putObject(new PutObjectRequest(bucket, videoname, videofile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        boardWriteRequestDTO.setVideourl(s3Client.getUrl(bucket, videoname).toString());

        return boardWriteRequestDTO;
    }


    //파일 삭제
//    public void fileDelete(List<String> fileName) {
//        try {
//            for (int i=0; i <=fileName.size(); i++){
//                //s3Client.deleteObject(bucket, (fileName.get(i)).replace(File.separatorChar, '/'));
//
//                System.out.println("1");
//                System.out.println(fileName.get(i));
//                DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName.get(i));
//
//                System.out.println("2");
//                s3Client.deleteObject(request);
//            }
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//        }catch (SdkClientException e) {
//            e.printStackTrace();
//        }
//
//    }


    // 파일 하나 삭제
    public void fileDeleteOne(String fileName) {
        boolean isExistObject = s3Client.doesObjectExist(bucket, fileName);
        if (isExistObject) {
            try {
                System.out.println(fileName);
                s3Client.deleteObject(bucket, fileName);

            }catch (AmazonServiceException e){
                System.out.println(e.getErrorMessage());
            }
            catch (SdkClientException e) {
                e.printStackTrace();
            }

        }
//        try {
//            System.out.println(fileName);
//            s3Client.deleteObject(bucket, fileName);
//        }
////        }catch (Exception e) {
////            Sentry.captureException(e);
////        }
//        catch (AmazonServiceException e){
//            System.out.println(e.getErrorMessage());
//        }
//        catch (SdkClientException e) {
//            e.printStackTrace();
//        }
//        }
    }
}


