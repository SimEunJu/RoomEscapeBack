package com.sej.escape.service.security;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.entity.constants.SocialLogin;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        String email = null;
        switch (clientName){
            case "Google":
                email = oAuth2User.getAttribute("email");
        }

        Optional<Member> memberOpt = memberService.findMemberByEmail(email);

        Member member = null;
        if(memberOpt.isEmpty()) {

            Set<MemberRole> roles = new HashSet<>();
            roles.add(MemberRole.USER);

            MemberReqDto memberReqDto = MemberReqDto.builder()
                    .email(email)
                    .rawPassword(email)
                    .socialLogin(SocialLogin.valueOf(clientName.toUpperCase()))
                    .roles(roles)
                    .build();

            member = memberService.registerMember(memberReqDto);
            //throw new OAuth2SignUpAttemptException(email);
        }else{
            member = memberOpt.get();
        }

        MemberDto memberDto = memberMapper.mapEntityToDto(member);

        memberDto.setOauthAttrs(oAuth2User.getAttributes());

        // TODO: 정지 회원인 경우 알려야
        return memberDto;

    }
}
