package com.spring.sharepod.controller;


import com.spring.sharepod.exception.TokenError.CAccessDeniedException;
import com.spring.sharepod.exception.TokenError.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@RequiredArgsConstructor
//@RestController
//public class ExceptionController {
//
//    @GetMapping("/exception/entrypoint")
//    public ResponseEntity<CAuthenticationEntryPointException> entrypointException() {
//        throw new CAuthenticationEntryPointException(REGISTER_NULL_NICKNAME);
//    }
//
//    @GetMapping("/exception/accessDenied")
//    public ResponseEntity<CAccessDeniedException> accessDeniedException() {
//        throw new CAccessDeniedException(REGISTER_NULL_NICKNAME);
//    }
//}

