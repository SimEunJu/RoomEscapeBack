package com.sej.escape.dto.zim;

import com.sej.escape.constants.dto.ZimType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZimReqDto {

    @ApiModelProperty("아이디") private Long id;

    @NotNull
    @ApiModelProperty("찜 사용하는 도메인 아이디") private Long referId;

    @NotNull
    @ApiModelProperty("종류") private ZimType type;

    @NotNull
    @ApiModelProperty("활성화 여부") private Boolean isChecked;

}
