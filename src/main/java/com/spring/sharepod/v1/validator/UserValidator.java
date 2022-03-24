package com.spring.sharepod.v1.validator;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.UserRequestDto;
import com.spring.sharepod.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.spring.sharepod.exception.CommonError.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 validator
    public void validateUserRegisterData(UserRequestDto.Register userRegisterRequestDto) {

        //userRegisterRequestDto 중에 하나라도 빵구
        if(userRegisterRequestDto.getUsername() == null){
            throw new ErrorCodeException(REGISTER_NULL_USERNAME);
        }
        if(userRegisterRequestDto.getNickName() == null){
            throw new ErrorCodeException(REGISTER_NULL_NICKNAME);
        }
        if(userRegisterRequestDto.getPassword() == null){
            throw new ErrorCodeException(REGISTER_NULL_PASSWORD);
        }
        if(userRegisterRequestDto.getPasswordCheck() == null){
            throw new ErrorCodeException(REGISTER_NULL_PASSWORDCHECK);
        }
        if(userRegisterRequestDto.getUserRegion() == null){
            throw new ErrorCodeException(REGISTER_NULL_MAPDATA);
        }
        if(userRegisterRequestDto.getUserImg() == null){
            throw new ErrorCodeException(REGISTER_NULL_USERIMG);
        }


        // 유저네임(이메일) 중복 확인
        Optional<User> findEmail = userRepository.findByUsername(userRegisterRequestDto.getUsername());
        if (findEmail.isPresent()) {
            throw new ErrorCodeException(EMAIL_DUPLICATE);
        }

        // 닉네임 중복 확인
        Optional<User> findUsername = userRepository.findByNickName(userRegisterRequestDto.getNickName());
        if (findUsername.isPresent()) {
            throw new ErrorCodeException(USERNAME_DUPLICATE);
        }

        // 이메일(유저네임) 유효성 확인
        if(!Pattern.matches("^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$" , userRegisterRequestDto.getUsername())){
            throw new ErrorCodeException(EMAIL_VALIDATE);
        }

        // 비밀번호 닉네임 포함 확인
        if (userRegisterRequestDto.getPassword().contains(userRegisterRequestDto.getUsername())) {
            throw new ErrorCodeException(PASSWORD_INCLUDE_USERNAME);
        }

        // 비밀번호 길이 확인
        if (userRegisterRequestDto.getPassword().length() < 4) {
            throw new ErrorCodeException(PASSWORD_LENGTH);
        }

        // 비밀번호 비밀번호 확인과 일치 확인
        if (!Objects.equals(userRegisterRequestDto.getPassword(), userRegisterRequestDto.getPasswordCheck())) {
            throw new ErrorCodeException(PASSWORD_COINCIDE);
        }

    }



    //유저 id에 대한 user가 존재하는지에 대한 판단
    public User ValidByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

    }

    //유저네임(이메일) 유무 확인
    public void ValidModifiedUser(UserRequestDto.Modify modifyRequestDTO) {
        // 유저네임(이메일) 유무 확인
        userRepository.findByUsername(modifyRequestDTO.getUsername()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

    }


    //회원 탈퇴
    public User ValidByUserDelete(Long userid, UserRequestDto.Login userLoginRequest) {
        //userid가 있는지 확인
        User user = userRepository.findById(userid).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //이메일이 맞는지 확인
        System.out.println(user.getUsername());
        System.out.println(userLoginRequest.getUsername());
        if(!Objects.equals(user.getUsername(), userLoginRequest.getUsername())){
            throw new ErrorCodeException(USER_NOT_FOUND);
        }
        //패스워드가 맞는지 확인
        //비밀번호 다르면
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new ErrorCodeException(ErrorCode.PASSWORD_COINCIDE);
        }
        return user;
    }

    //로그인 request 관련 예외처리
    public void ValidLoginRequest(UserRequestDto.Login userLoginRequest){
        if(userLoginRequest.getPassword() == null || userLoginRequest.getUsername() == null){
            throw new ErrorCodeException(LOGIN_DATA_NOT_EXIST);
        }
    }

}


