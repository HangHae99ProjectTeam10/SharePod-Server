package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // 유저 생성하기(회원가입)(JSON)
    @PostMapping(value = "/user/register")
    public BasicResponseDTO createUser(@RequestPart UserRegisterRequestDto userRegisterRequestDto,@RequestPart MultipartFile[] imgFiles) throws IOException {

        String UPLOAD_PATH = "D:\\";
        for(int i = 0; i<imgFiles.length;i++){
            System.out.println(imgFiles[i]);
            MultipartFile File = imgFiles[i];

            File fileSave = new File(UPLOAD_PATH, i + ""); // ex) fileId.jpg
            if(!fileSave.exists()) { // 폴더가 없을 경우 폴더 만들기
                fileSave.mkdirs();
            }

            imgFiles[i].transferTo(fileSave); // fileSave의 형태로 파일 저장
        }
        return userService.createUser(userRegisterRequestDto);
    }
}
