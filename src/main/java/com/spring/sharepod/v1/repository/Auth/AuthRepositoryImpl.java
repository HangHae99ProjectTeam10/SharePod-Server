package com.spring.sharepod.v1.repository.Auth;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.Auth.AuthDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QAuthImg.authImg;

@RequiredArgsConstructor
@Repository
public class AuthRepositoryImpl implements AuthRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean authtest(Long authid) {
        return authtestcheck(authid);
    }

    @Override
    public List<AuthDataResponseDto> getauthresponse(Long authId) {
        return getauthresponselist(authId).fetch();
    }

    private Boolean authtestcheck(Long authid) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(authImg)
                .where(authImg.auth.id.eq(authid).and(authImg.checkImgBox.eq(false)))
                .fetchFirst();
        return fetchOne != null;
    }

    private JPAQuery<AuthDataResponseDto> getauthresponselist(Long authId) {
        return jpaQueryFactory.select(Projections.bean(AuthDataResponseDto.class,
                        authImg.id,
                        authImg.authImgUrl,
                        authImg.checkImgBox
                ))
                .from(authImg)
                .where(authImg.auth.id.eq(authId));
    }


}
