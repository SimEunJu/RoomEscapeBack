package com.sej.escape.dto.member;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberUpdateReqDto {
    private String nickname;
}
