package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Auth.AuthCheckReUploadRequestDto;
import com.spring.sharepod.dto.response.Auth.AuthDataAllResponseDTO;
import com.spring.sharepod.dto.response.Auth.AuthDataResponseDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.repository.AuthRepository;
import com.spring.sharepod.repository.AuthimgboxRepository;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.validator.AuthValidator;
import com.spring.sharepod.validator.BoardValidator;
import com.spring.sharepod.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final AuthimgboxRepository authimgboxRepository;
    private final BoardRepository boardRepository;
    private final BoardValidator boardValidator;
    private final AuthValidator authValidator;
    private final TokenValidator tokenValidator;
    private final AuthImgService authImgService;
    private final AwsS3Service awsS3Service;
    private final S3Service s3Service;


    //buyer가 인증 이미지 업로드
    @PostMapping("/auth/img/{userid}/{authimgboxid}")
    public BasicResponseDTO AuthImgUpload(@PathVariable Long userid, @PathVariable Long authimgboxid, @RequestPart MultipartFile authfile,@AuthenticationPrincipal User user) throws IOException {
        // 토큰과 userid 일치하는지 확인
        tokenValidator.userIdCompareToken(userid,user.getId());

        //인증 사진 저장 및 유저 정보 맞는지 확인
        String s3authimgurl = s3Service.authimgboxs3(userid, authimgboxid, authfile);

        //authimgbox 저장 및 반환
        return authImgService.authimguploadService(userid, authimgboxid, s3authimgurl, user);
    }


    //이미지 인증 창 데이터
    @javax.transaction.Transactional
    public AuthDataAllResponseDTO dataAllResponseDTO(@PathVariable Long authid) {
        //authid로 auth가 없다면 error 메시지 호출
        Auth auth = authValidator.ValidAuthByAuthId(authid);


        boolean allauthcheck = true;
        //data[] 값 넣어주기
        List<AuthDataResponseDTO> authDataResponseDTOList = new ArrayList<>();
        for (int i = 0; i < auth.getAuthimgboxList().size(); i++) {
            //allauthcheck 하나라도 false 있으면 false로 교체
            if (!auth.getAuthimgboxList().get(i).isImgboxcheck()) {
                allauthcheck = false;
            }

            authDataResponseDTOList.add(AuthDataResponseDTO.builder()
                    .authimgboxid(auth.getAuthimgboxList().get(i).getId())
                    .imgurl(auth.getAuthimgboxList().get(i).getImgurl())
                    .imgboxcheck(auth.getAuthimgboxList().get(i).isImgboxcheck())
                    .build());
        }

        return AuthDataAllResponseDTO.builder()
                .result("success")
                .msg("사진 데이터 전송 성공")
                .seller(auth.getAuthseller().getId())
                .buyer(auth.getAuthbuyer().getId())
                .authcheck(allauthcheck)
                .rentalstart(auth.getRentalstart())
                .rentalend(auth.getRentalend())
                .data(authDataResponseDTOList)
                .build();

    }


    //빌려준 사람만의 기능, 인증 성공 or 실패
    @Transactional
    public Long CheckReuploadBoard(AuthCheckReUploadRequestDto authCheckReUploadRequestDto) {

        //주어진 id에 대해서 auth가 존재하는지 확인
        Auth auth = authValidator.ValidAuthByAuthId(authCheckReUploadRequestDto.getAuthid());

        // authid로 board 찾기
        Board board = boardValidator.ValidByBoardId(auth.getBoard().getId());

        // auth의 sellerid와 request의 id가 일치하는지 확인
        authValidator.ValidAuthBySellerIdEqualRequestId(authCheckReUploadRequestDto, auth.getAuthseller().getId());


        if (authCheckReUploadRequestDto.isAuthreupload()) {
            board.setAppear(true);
        } else {
            // 자식 엔터티부터 지우기 -> 추후 cascade로 한번에 처리 예정
            authimgboxRepository.deleteByAuthId(auth.getId());
            authRepository.deleteById(auth.getId());
            boardRepository.deleteById(auth.getBoard().getId());
        }

        // 게시글 번호 리턴
        return board.getId();

    }

}
