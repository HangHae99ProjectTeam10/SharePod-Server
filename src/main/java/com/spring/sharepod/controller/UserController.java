package com.spring.sharepod.controller;

import com.spring.sharepod.dto.RegisterRequestDto;
import com.spring.sharepod.model.ErrorMessage;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 회원 로그인 페이지
    @GetMapping("/api/login")
    public String login() {
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/api/register")
    public String signup() {
        return "signup";
    }



    // 회원 가입 요청 처리
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            bindingResult.getAllErrors().forEach(objectError -> {
                errorMessages.add(new ErrorMessage(objectError.getDefaultMessage()));
            });
            return ResponseEntity.badRequest().body(errorMessages);
        }

            userService.registerUser(requestDto);
            return ResponseEntity.ok().body("");
    }
}

