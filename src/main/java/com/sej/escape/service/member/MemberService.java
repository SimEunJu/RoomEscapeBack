package com.sej.escape.service.member;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.constants.SocialLogin;
import com.sej.escape.repository.MemberRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public MemberDto getMemberInfo(){
        return authenticationUtil.getAuthUser();
    }

    public MemberDto updateMember(MemberDto memberDto){
        Member member = authenticationUtil.getAuthUserEntity();
        member.setNickname(memberDto.getNickname());
        member = memberRepository.save(member);
        return memberDto;
    }

    public void withdrawalMember(){
        Member member = authenticationUtil.getAuthUserEntity();
        memberRepository.withdrawal(member);
    }
}
