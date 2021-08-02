package com.sej.escape.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberUpdateReqDto {
    @ApiModelProperty("별명") private String nickname;
}
