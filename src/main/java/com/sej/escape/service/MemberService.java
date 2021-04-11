package com.sej.escape.service;

import com.sej.escape.dto.MemberDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.entity.constants.SocialLogin;
import com.sej.escape.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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

//@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;

    public void RegisterMember(MemberDto memberDto){
        Set<MemberRole> roles = new HashSet<>();
        roles.add(MemberRole.USER);

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .socialLogin(memberDto.getSocialLogin())
                .roles(roles)
                .nickname(memberDto.getNickname())
                .build();

        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberOpt = memberRepository.findMemberByEmail(username);

        if(memberOpt.isEmpty()) {
            throw new UsernameNotFoundException(username+": 가입되지 않았습니다.");
        }

        Member member = memberOpt.get();

        Collection<SimpleGrantedAuthority> authorities = member.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());

        MemberDto memberDto = new MemberDto(member.getEmail(), member.getPassword(), authorities);
        memberDto.setId(member.getId());
        memberDto.setSocialLogin(member.getSocialLogin());
        memberDto.setName(member.getMemberName());
        memberDto.setNickname(member.getNickname());
        // TODO: 정지 회원인 경우 알려야
        return memberDto;
    }
}
