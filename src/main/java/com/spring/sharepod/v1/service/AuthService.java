package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.v1.dto.request.AuthRequestDto;
import com.spring.sharepod.v1.dto.response.AuthResponseDto;
import com.spring.sharepod.v1.repository.AuthImgRepository;
import com.spring.sharepod.v1.repository.AuthRepository;
import com.spring.sharepod.v1.repository.Board.BoardRepository;
import com.spring.sharepod.v1.validator.AuthValidator;
import com.spring.sharepod.v1.validator.BoardValidator;
import com.spring.sharepod.v1.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final BoardRepository boardRepository;
    private final AuthImgRepository authImgRepository;
    private final BoardValidator boardValidator;
    private final AuthValidator authValidator;
    private final TokenValidator tokenValidator;
    private final AuthImgService authImgService;
    private final AwsS3Service awsS3Service;


    //20번 API 이미지 인증 창 데이터(구현 완료)
    @Transactional
    public AuthResponseDto.AuthDataAll dataAllResponseDTO(@PathVariable Long authid) {
        //authid로 auth가 없다면 error 메시지 호출
        Auth auth = authValidator.ValidAuthByAuthId(authid);


        boolean allauthcheck = true;
        //data[] 값 넣어주기
        List<AuthResponseDto.AuthData> authDataResponseDTOList = new ArrayList<>();
        for (int i = 0; i < auth.getAuthImgList().size(); i++) {
            //allauthcheck 하나라도 false 있으면 false로 교체
            if (!auth.getAuthImgList().get(i).isCheckImgBox()) {
                allauthcheck = false;
            }

            authDataResponseDTOList.add(AuthResponseDto.AuthData.builder()
                    .authImgId(auth.getAuthImgList().get(i).getId())
                    .authImgUrl(auth.getAuthImgList().get(i).getAuthImgUrl())
                    .authImgCheck(auth.getAuthImgList().get(i).isCheckImgBox())
                    .build());
        }

        auth.setSellectAllImg(allauthcheck);

        return AuthResponseDto.AuthDataAll.builder()
                .result("success")
                .msg("사진 데이터 전송 성공")
                .sellerId(auth.getAuthSeller().getId())
                .buyerId(auth.getAuthBuyer().getId())
                .authAllCheck(allauthcheck)
                .startRental(auth.getStartRental())
                .endRental(auth.getEndRental())
                .data(authDataResponseDTOList)
                .build();
    }


    //빌려준 사람만의 기능, 재업로드
    @Transactional
    public AuthResponseDto.AuthReUploadDTO CheckReuploadBoard(AuthRequestDto.AuthCheckReUpload authCheckReUploadRequestDto) {

        //주어진 id에 대해서 auth가 존재하는지 확인
        Auth auth = authValidator.ValidAuthByAuthId(authCheckReUploadRequestDto.getAuthId());

        // authid로 board 찾기
        Board board = boardValidator.ValidByBoardId(auth.getBoard().getId());

        // auth의 sellerid와 request의 id가 일치하는지 확인
        authValidator.ValidAuthBySellerIdEqualRequestId(authCheckReUploadRequestDto, auth.getAuthSeller().getId());


        if (authCheckReUploadRequestDto.isAuthReUpload()) {
            board.setAppear(true);
        } else {
            // 자식 엔터티부터 지우기 -> 추후 cascade로 한번에 처리 예정
            authImgRepository.deleteByAuthId(auth.getId());
            authRepository.deleteById(auth.getId());
            boardRepository.deleteById(auth.getBoard().getId());
        }

        //reupload에 따라서 메시지 다르게 호출
        String result = authValidator.ValidAuthByReupload(authCheckReUploadRequestDto.isAuthReUpload());


        return AuthResponseDto.AuthReUploadDTO.builder()
                .result("success")
                .msg(board.getId() +"번 "+ result)
                .authReupload(authCheckReUploadRequestDto.isAuthReUpload())
                .sellerId(authCheckReUploadRequestDto.getSellerId())
                .authId(authCheckReUploadRequestDto.getAuthId())
                .build();

    }

}
