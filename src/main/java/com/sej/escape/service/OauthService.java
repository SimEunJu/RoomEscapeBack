package com.sej.escape.service;

import com.sej.escape.dto.MemberDto;
import com.sej.escape.entity.Member;
import com.sej.escape.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        String email = null;
        switch (clientName){
            case "Google":
                email = oAuth2User.getAttribute("email");
        }

        Optional<Member> memberOpt = memberRepository.findMemberByEmail(email);

        if(memberOpt.isEmpty()) {
            throw new UsernameNotFoundException(email+": 가입되지 않았습니다.");
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

        memberDto.setOauthAttrs(oAuth2User.getAttributes());
        // TODO: 정지 회원인 경우 알려야
        return memberDto;

    }
}
