package com.spring.sharepod.validator;

import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.spring.sharepod.exception.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUserRegisterData(UserRegisterRequestDto userRegisterRequestDto) {
        // 유저네임(이메일) 중복 확인
        Optional<User> findEmail = userRepository.findByUsername(userRegisterRequestDto.getUsername());
        if (findEmail.isPresent()) {
            throw new ErrorCodeException(EMAIL_DUPLICATE);
        }

        // 닉네임 중복 확인
        Optional<User> findUsername = userRepository.findByNickname(userRegisterRequestDto.getNickname());
        if (findUsername.isPresent()) {
            throw new ErrorCodeException(USERNAME_DUPLICATE);
        }

        // 이메일(유저네임) 유효성 확인
        if (!Pattern.matches("^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", userRegisterRequestDto.getUsername())) {
            throw new ErrorCodeException(EMAIL_VALIDATE);
        }

//        // 비밀번호 닉네임 포함 확인
//        if (userRegisterRequestDto.getPassword().contains(userRegisterRequestDto.getUsername())) {
//            throw new ErrorCodeException(PASSWORD_INCLUDE_USERNAME);
//        }

//        // 비밀번호 길이 확인
//        if (userRegisterRequestDto.getPassword().length() < 4) {
//            throw new ErrorCodeException(PASSWORD_LENGTH);
//        }
        // 비밀번호 비밀번호 확인과 일치 확인
        if (!Objects.equals(userRegisterRequestDto.getPassword(), userRegisterRequestDto.getPasswordcheck())) {
            throw new ErrorCodeException(PASSWORD_COINCIDE);
        }
    }

    public void validateUserChange(UserModifyRequestDTO modifyRequestDTO) {
        // 유저네임(이메일) 유무 확인
        userRepository.findByUsername(modifyRequestDTO.getUsername()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

    }
}
