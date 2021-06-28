package com.sej.escape.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class MemberRes {

    private Long id;
    private String nickname;
    private String email;
    private String role;
}
