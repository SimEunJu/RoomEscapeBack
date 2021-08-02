package com.sej.escape.service.member;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberReqDto;
import com.sej.escape.dto.member.MemberResDto;
import com.sej.escape.dto.member.MemberUpdateReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.constants.SocialLogin;
import com.sej.escape.repository.MemberRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtil authenticationUtil;
    private final ModelMapper modelMapper;

    public Optional<Member> findMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }

    public Member registerMember(MemberReqDto memberReqDto){
        Member memberNew = Member.builder()
                .email(memberReqDto.getEmail())
                .password(passwordEncoder.encode(memberReqDto.getRawPassword()))
                .socialLogin(memberReqDto.getSocialLogin())
                .roles(memberReqDto.getRoles())
                .build();
        return memberRepository.save(memberNew);
    }

    public String transformRoles(Collection<GrantedAuthority> authorities){
        // role 1개만 가정
        List<String> roles = authorities
                .stream()
                .map(auth -> auth.getAuthority().substring(5))
                .collect(Collectors.toList());

        return roles.get(0);
    }

    public MemberResDto updateMember(MemberUpdateReqDto reqDto) {
        Member member = authenticationUtil.getAuthUserEntity();
        member.setNickname(reqDto.getNickname());
        member = memberRepository.save(member);

        authenticationUtil.updateUser(member);

        MemberResDto memberDto = modelMapper.map(member, MemberResDto.class);
        memberDto.setRole(transformRoles(authenticationUtil.getAuthUser().getAuthorities()));
        return memberDto;
    }

    public void withdrawalMember(){
        Member member = authenticationUtil.getAuthUserEntity();
        memberRepository.withdrawal(member);
    }
}
