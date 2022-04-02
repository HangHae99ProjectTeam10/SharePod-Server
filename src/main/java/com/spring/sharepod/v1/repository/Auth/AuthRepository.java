package com.spring.sharepod.v1.repository.Auth;

import com.spring.sharepod.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long>, AuthRepositoryCustom {

}


