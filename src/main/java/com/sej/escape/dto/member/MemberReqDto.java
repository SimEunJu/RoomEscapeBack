package com.sej.escape.dto.member;

import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.entity.constants.SocialLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class MemberReqDto {

    private String email;
    private String rawPassword;
    private SocialLogin socialLogin;
    private Set<MemberRole> roles;
}
