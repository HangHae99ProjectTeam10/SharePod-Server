package com.spring.sharepod.exception;

import com.spring.sharepod.entity.Authimgbox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request - 잘못된 요청 (REQUEST 가 정확한 정보가 아님).
    // 403 Forbidden - 해당 요청에 대한 권한이 없음.
    // 404 Not Found - 해당 RESOURECE 를 찾을 수 없음 (경로가 없거나 그런거임).

    // 회원가입 관련 에러 모음
    USERNAME_VALIDATE(HttpStatus.BAD_REQUEST, "400_Register_1", "유저네임은 최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성해야 합니다." ),
    USERNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "400_Register_2", "이미 존재하는 유저네임 입니다."),

    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "400_Register_3", "이미 가입한 E-MAIL 입니다."),
    EMAIL_VALIDATE(HttpStatus.BAD_REQUEST, "400_Register_4", "이메일 형식이 아닙니다."),

    PASSWORD_INCLUDE_USERNAME(HttpStatus.BAD_REQUEST, "400_Register_4", "비밀번호는 닉네임을 포함하지 못합니다."),
    PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "400_Register_5", "비밀번호는 최소 4자 이상입니다."),
    PASSWORD_COINCIDE(HttpStatus.BAD_REQUEST, "400_Register_6", "비밀번호를 다시 한번 확인해주세요"),


    // 로그인 관련 에러 모음
    LOGIN_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_Login_1", "가입되지 않은 E-MAIL 입니다."),
    LOGIN_PASSWORD_NOT_MATCH(HttpStatus.NOT_FOUND, "404_Login_2", "비밀번호가 일치하지 않습니다."),


    // 유저 관련 에러 모음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_User_1", "해당 유저는 존재하지 않습니다." ),


    // 게시판 관련 에러 모음
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404_Board_1", "해당 게시글이 존재하지 않습니다."),
    BOARD_NOT_FOUND2(HttpStatus.NOT_FOUND, "404_Board_4", "아이디가 일치하지 않습니다.."),
    BOARD_EDIT_OR_DELETE_NOT_MATCH(HttpStatus.FORBIDDEN, "403_Board_2", "게시글의 생성자만 게시글을 수정 혹은 삭제할 수 있습니다."),
    BOARD_IMG_NOT_EXIST(HttpStatus.FORBIDDEN, "403_Board_3", "게시하려는 이미지의 주소가 없습니다."),

    // 좋아요 관련 에러 모음
    LIKE_EXIST(HttpStatus.BAD_REQUEST, "400_Like_1", "이미 좋아요를 눌렀습니다."),
    LIKE_NOT_EXIST(HttpStatus.BAD_REQUEST, "400_Like_2", "삭제하려는 좋아요가 존재하지 않습니다."),


    // 거래 요청 관련 에러 모음
    RESERVATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "400_Reservation_1", "이미 거래 요청이 존재합니다."),


    // 인증사진 관련 에러 모음
    AUTHIMGBOX_NOT_EXIST(HttpStatus.BAD_REQUEST, "404_AuthImgBox_1", "이미 거래 요청이 존재합니다."),
    AUTHIMGBOX_NOT_SELLER(HttpStatus.BAD_REQUEST, "400_AuthImgBox_2", "판매자가 아닙니다."),
    AUTHIMGBOX_NOT_BUYER(HttpStatus.NOT_FOUND, "404_AUTHIMGBOX_1", "해당 물건의 구매자가 아닙니다."),

    //알림 삭제 관련 에러 모음
    NOTICE_NOT_EXIST(HttpStatus.NOT_FOUND, "404_Notice_1", "해당 알림는 존재하지 않습니다."),


    //인증 페이지 관련 에러 모음
    AUTH_NOT_EXIST(HttpStatus.NOT_FOUND, "404_AUTH_1", "해당 인증 페이지는 존재하지 않습니다."),
    AUTH_NOT_SELLER(HttpStatus.NOT_FOUND, "400_AUTH_1", "해당 기능은 판매자만 가능합니다.");

    //해당 물건의 구매자가 아닙니다.
    AUTHIMGBOX_NOT_BUYER(HttpStatus.NOT_FOUND, "404_AUTHIMGBOX_1", "해당 물건의 구매자가 아닙니다.");


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
