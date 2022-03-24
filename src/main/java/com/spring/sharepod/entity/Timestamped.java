package com.spring.sharepod.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@MappedSuperclass // 맴버 변수 컬림이 되도록 함
@EntityListeners(AuditingEntityListener.class) // 변경 되었을 대 자동 기록.

public abstract class Timestamped {
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;

    public abstract Collection<? extends GrantedAuthority> getAuthorities();

    public abstract String getUsername();

    public abstract boolean isAccountNonExpired();

    public abstract boolean isAccountNonLocked();

    public abstract boolean isCredentialsNonExpired();

    public abstract boolean isEnabled();
}