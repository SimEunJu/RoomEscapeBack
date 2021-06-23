package com.sej.escape.controller;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberRes;
import com.sej.escape.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberRes> getMember(Authentication authentication){
        MemberRes payload = null;
        if(authentication == null) return ResponseEntity.ok(payload);

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();
        // role 1개만 가정
        List<String> roles = memberDto.getAuthorities().stream().map(auth -> auth.getAuthority().substring(5)).collect(Collectors.toList());

        payload = MemberRes.builder()
                .id(memberDto.getId())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .role(roles.get(0))
                .build();
        return ResponseEntity.ok(payload);
    }

    @PatchMapping
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto){
       MemberDto memberDtoUpdated = memberService.updateMember(memberDto);
       return ResponseEntity.ok(memberDtoUpdated);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdrawalMember(){
        memberService.withdrawalMember();
        return ResponseEntity.ok(null);
    }
}
