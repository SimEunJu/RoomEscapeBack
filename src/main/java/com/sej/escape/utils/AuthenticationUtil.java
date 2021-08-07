package com.sej.escape.utils;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.entity.Member;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public boolean isAuthenticated() {
        Authentication authentication = this.getAuthentication();
        return authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser";
    }

    public boolean isSameUser(long id){
        if(getAuthUser().getId() == id) return true;
        return false;
    }

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public MemberDto getAuthUser(){
        if(!isAuthenticated()) throw new AuthenticationCredentialsNotFoundException("login is required");
        MemberDto memberDto = (MemberDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberDto;
    }

    public Member getAuthUserEntity(){
        MemberDto memberDto = getAuthUser();
        return Member.builder()
                .id(memberDto.getId())
                .email(memberDto.getEmail())
                .build();
    }

    public void updateUser(Member member){
        MemberDto memberDto = this.getAuthUser();
        memberDto.setNickname(member.getNickname());
    }
}
