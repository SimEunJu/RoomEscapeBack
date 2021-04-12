package com.sej.escape.error.exception.security;

import com.sej.escape.error.exception.BusinessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// 소셜 로그인 시도했으나, db에 해당 회원이 없는 경우 -> 회원가입을 위한 추가 정보 기입 redirect
public class OAuth2SignUpAttemptException extends UsernameNotFoundException {

    public OAuth2SignUpAttemptException(String email){
        super("미가입 회원 소셜 로그인 시도 :"+email);
    }
}

