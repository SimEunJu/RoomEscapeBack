package com.sej.escape.controller;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberDto> getMemberInfo(){
        MemberDto memberDto = memberService.getMemberInfo();
        return ResponseEntity.ok(memberDto);
    }

    @PatchMapping
    public ResponseEntity<MemberDto> updateMemberInfo(@RequestBody MemberDto memberDto){
       MemberDto memberDtoUpdated = memberService.updateMember(memberDto);
       return ResponseEntity.ok(memberDtoUpdated);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdrawalMember(){
        memberService.withdrawalMember();
        return ResponseEntity.ok(null);
    }
}
