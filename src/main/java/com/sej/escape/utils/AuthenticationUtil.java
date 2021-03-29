package com.sej.escape.utils;

import com.sej.escape.dto.MemberDto;
import com.sej.escape.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Authentication getAuthUser(){
        MemberDto memberDto = (MemberDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
