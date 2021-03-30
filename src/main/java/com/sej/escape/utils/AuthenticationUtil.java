package com.sej.escape.utils;

import com.sej.escape.dto.MemberDto;
import com.sej.escape.entity.Member;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class AuthenticationUtil {

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public MemberDto getAuthUser(){
        Authentication authentication = this.getAuthentication();
        if(!authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser") throw new AuthenticationCredentialsNotFoundException("login is required");
        MemberDto memberDto = (MemberDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberDto;
    }
}
