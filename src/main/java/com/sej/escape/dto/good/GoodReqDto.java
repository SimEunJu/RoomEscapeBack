package com.sej.escape.dto.good;

import com.sej.escape.constants.dto.GoodType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
public class GoodReqDto {

    @ApiModelProperty("아이디") private Long id;

    @NotNull
    @ApiModelProperty("좋아요 사용하는 도메인 아이디") private Long referId;

    @NotNull
    @ApiModelProperty("종류") private GoodType type;

    @NotNull
    @ApiModelProperty("좋아요 여부") private Boolean isChecked;

}
