package com.sej.escape.controller;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberResDto;
import com.sej.escape.dto.member.MemberUpdateReqDto;
import com.sej.escape.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
@Api("인증")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("사용자 정보")
    @GetMapping
    public ResponseEntity<MemberResDto> getMember(@ApiIgnore Authentication authentication){

        if(authentication == null) return ResponseEntity.ok(null);

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        MemberResDto payload = MemberResDto.builder()
                .id(memberDto.getId())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .role(memberService.transformRoles(memberDto.getAuthorities()))
                .build();

        return ResponseEntity.ok(payload);
    }

    @ApiOperation("사용자 정보 업데이트")
    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MemberResDto> updateMember(@RequestBody MemberUpdateReqDto updateReqDto){
        MemberResDto memberDtoUpdated = memberService.updateMember(updateReqDto);
        return ResponseEntity.ok(memberDtoUpdated);
    }

    @ApiOperation("탈퇴")
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> withdrawalMember(){
        memberService.withdrawalMember();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(null);
    }
}
