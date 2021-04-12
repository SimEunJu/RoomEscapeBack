package com.sej.escape.service.member;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.entity.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final ModelMapper modelMapper;

    public MemberDto mapEntityToDto(Member member){

        Collection<SimpleGrantedAuthority> authorities = member.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());

        MemberDto memberDto = new MemberDto(member.getEmail(), member.getPassword(), authorities);
        memberDto.setId(member.getId());
        memberDto.setSocialLogin(member.getSocialLogin());
        memberDto.setName(member.getMemberName());
        memberDto.setNickname(member.getNickname());

        return memberDto;
    }
}
