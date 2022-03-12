package com.spring.sharepod.service;


import com.spring.sharepod.dto.response.Auth.AuthDataAllResponseDTO;
import com.spring.sharepod.dto.response.Auth.AuthDataResponseDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Authimgbox;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.AuthRepository;
import com.spring.sharepod.repository.AuthimgboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final AuthimgboxRepository authimgboxRepository;

    @Transactional
    public BasicResponseDTO authimguploadService(Long authimgboxid, String imgurl) {
        Authimgbox authimgbox = authimgboxRepository.findById(authimgboxid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.AUTHIMGBOX_NOT_EXIST));

        //이미지 DB 넣어주기
        authimgbox.update(imgurl);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("사진 등록성공")
                .build();
    }

    @Transactional
    public AuthDataAllResponseDTO dataAllResponseDTO(@PathVariable Long authid) {
        Auth auth = authRepository.findById(authid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.AUTHIMGBOX_NOT_EXIST));

        boolean allauthcheck = true;
        //data[] 값 넣어주기
        List<AuthDataResponseDTO> authDataResponseDTOList = new ArrayList<>();
        for (int i = 0; i < auth.getAuthimgboxList().size(); i++) {
            //allauthcheck 하나라도 false 있으면 false로 교체
            if(!auth.getAuthimgboxList().get(i).isImgboxcheck()){
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
}
