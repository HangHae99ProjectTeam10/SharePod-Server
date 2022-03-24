package com.spring.sharepod.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
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
                System.out.println("keys            " +    fileName.get(i));
                String DeleteFileName = fileName.get(i);

                amazonS3.deleteObject(bucket,DeleteFileName);

                System.out.println("2");
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }catch (SdkClientException e) {
            e.printStackTrace();
        }

    }
}
