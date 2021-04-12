package com.sej.escape.service.security;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.service.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberOpt = memberService.findMemberByEmail(username);

        if(memberOpt.isEmpty()) {
            throw new UsernameNotFoundException(username+": 가입되지 않았습니다.");
        }

        Member member = memberOpt.get();
        MemberDto memberDto = memberMapper.mapEntityToDto(member);

        // TODO: 정지 회원인 경우 알려야
        return memberDto;
    }
}
