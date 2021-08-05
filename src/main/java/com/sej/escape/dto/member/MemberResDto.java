package com.sej.escape.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDto {

    @ApiModelProperty("아이디") private Long id;
    @ApiModelProperty("별명") private String nickname;
    @ApiModelProperty("이메일") private String email;
    @ApiModelProperty("권한") private String role;
}
