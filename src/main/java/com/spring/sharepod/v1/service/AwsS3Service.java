package com.spring.sharepod.v1.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.sharepod.entity.AuthImg;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.dto.request.UserRequestDto;
import com.spring.sharepod.v1.validator.AuthImgValidator;
import com.spring.sharepod.v1.validator.BoardValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private final UserValidator userValidator;
    private final BoardValidator boardValidator;
    private final AuthImgValidator authImgValidator;

    //회원 가입 시, 유저 프로필 사진 업로드
    public String upload(UserRequestDto.Register userRegisterRequestDto, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    //회원정보 수정 시, 파일이 바뀌었다면 진행되는 로직
    public String ModifiedProfileImg(String fileName, String userNickName, MultipartFile userimgfile) throws IOException {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        String modifiedFileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(userimgfile.getOriginalFilename());
        amazonS3.putObject(new PutObjectRequest(bucket, modifiedFileName, userimgfile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, modifiedFileName).toString();
    }

    //7번 API 회원 탈퇴하기
    // 회원 탈퇴 시, 진행되는 로직
    public void deleteProfileImg(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    //게시글 작성 시, 이미지 3개와 동영상 업로드
    public BoardRequestDto.WriteBoard boardUpload(BoardRequestDto.WriteBoard boardWriteRequestDTO,
                                                  MultipartFile[] imgFiles,
                                                  MultipartFile videoFile) throws IOException {
        //게시판 작성 validator
        boardValidator.validateBoardWrite(boardWriteRequestDTO, imgFiles, videoFile);

        //이미지 3개 처리
        String[] giveUrl = new String[3];
        for (int i = 0; i < giveUrl.length; i++) {
            if (Objects.equals(null, StringUtils.getFilenameExtension(imgFiles[i].getOriginalFilename()))){
                System.out.println("giveURL == null");
                giveUrl[i] = null;
            } else {
                System.out.println("giveURL != null");
                String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(imgFiles[i].getOriginalFilename());
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, imgFiles[i].getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                giveUrl[i] = amazonS3.getUrl(bucket, fileName).toString();
            }
        }

        boardWriteRequestDTO.setFirstImgUrl(giveUrl[0]);
        boardWriteRequestDTO.setSecondImgUrl(giveUrl[1]);
        boardWriteRequestDTO.setLastImgUrl(giveUrl[2]);

        if (videoFile.isEmpty()) {
            boardWriteRequestDTO.setVideoUrl(null);
        } else {
            //비디오 처리
            String videoName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(videoFile.getOriginalFilename());
            amazonS3.putObject(new PutObjectRequest(bucket, videoName, videoFile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            boardWriteRequestDTO.setVideoUrl(amazonS3.getUrl(bucket, videoName).toString());
        }
        return boardWriteRequestDTO;
    }

    //게시글 수정
    public BoardRequestDto.PatchBoard boardUpdate(Long boardId,
                                                  BoardRequestDto.PatchBoard patchRequestDTO,
                                                  MultipartFile[] imgFiles,
                                                  MultipartFile videoFile) throws IOException {
        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardValidator.ValidByBoardId(boardId);

        //게시판 작성 validator
        boardValidator.validateBoardUpdate(patchRequestDTO);

        //이미지 3개 처리
        User user = userValidator.ValidByUserId(patchRequestDTO.getUserId());

        String[] givenUrl = new String[3];
        for (int i = 0; i < imgFiles.length; i++) {
            if (Objects.equals(null, StringUtils.getFilenameExtension(imgFiles[i].getOriginalFilename()))) {
                if (i == 0) {
                    givenUrl[i] = board.getImgFiles().getFirstImgUrl();
                } else if (i == 1) {
                    givenUrl[i] = board.getImgFiles().getSecondImgUrl();
                } else {
                    givenUrl[i] = board.getImgFiles().getLastImgUrl();
                }

            } else {
                String modifiedBoardImg = "";
                if (i == 0) {
                    modifiedBoardImg = board.getImgFiles().getFirstImgUrl().substring(board.getImgFiles().getFirstImgUrl().lastIndexOf("/") + 1);
                } else if (i == 1) {
                    modifiedBoardImg = board.getImgFiles().getSecondImgUrl().substring(board.getImgFiles().getSecondImgUrl().lastIndexOf("/") + 1);
                } else {
                    modifiedBoardImg = board.getImgFiles().getLastImgUrl().substring(board.getImgFiles().getLastImgUrl().lastIndexOf("/") + 1);
                }

                // 게시글 삭제
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, modifiedBoardImg));
                String filename = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(imgFiles[i].getOriginalFilename());
                amazonS3.putObject(new PutObjectRequest(bucket, filename, imgFiles[i].getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                givenUrl[i] = amazonS3.getUrl(bucket, filename).toString();
            }
        }

        patchRequestDTO.setFirstImgUrl(givenUrl[0]);
        patchRequestDTO.setSecondImgUrl(givenUrl[1]);
        patchRequestDTO.setLastImgUrl(givenUrl[2]);

        //비디오파일 있는지 확인
        if (Objects.equals(videoFile.getOriginalFilename(), null)) {
            patchRequestDTO.setVideoUrl(board.getImgFiles().getVideoUrl());
        } else {
            //기존 이미지 삭제
            amazonS3.deleteObject(bucket, board.getImgFiles().getVideoUrl().substring(board.getImgFiles().getVideoUrl().lastIndexOf("/") + 1));
            //비디오 처리
            String videoname = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(videoFile.getOriginalFilename());
            amazonS3.putObject(new PutObjectRequest(bucket, videoname, videoFile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            patchRequestDTO.setVideoUrl(amazonS3.getUrl(bucket, videoname).toString());
        }

        return patchRequestDTO;
    }

    //파일 여러개 삭제(게시판 삭제)
    public void deleteBoardFiles(List<String> fileName) {
        try {
            for (int i = 0; i <= fileName.size() - 1; i++) {
                String DeleteFileName = fileName.get(i);
                amazonS3.deleteObject(bucket, DeleteFileName);
            }
        } catch (AmazonServiceException e) {
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    //21번 buyer가 인증 사진 업로드
    public String authImgCheck(Long userId, Long authImgId, MultipartFile authfile) throws IOException {

        //이미지박스 id가 존재하는지 확인
        AuthImg authImg = authImgValidator.ValidAuthImgById(authImgId);

        //구매자가 아이디가 일치하는지 확인
        authImgValidator.ValidAuthImgBoxIdEqualBuyerId(userId, authImg.getAuth().getAuthBuyer().getId());

        //이미지 s3 저장
        String imgName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(authfile.getOriginalFilename());
        amazonS3.putObject(new PutObjectRequest(bucket, imgName, authfile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, imgName).toString();
    }
}
