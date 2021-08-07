package com.sej.escape.dto.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.store.StoreDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.annotation.security.DenyAll;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeForListDto {

    @ApiModelProperty("아이디") protected long id;
    @ApiModelProperty("테마명") protected String name;
    @ApiModelProperty("별점 평균") protected double star;
    @ApiModelProperty("대표 이미지 url") protected String imgUrl;

    @JsonProperty("isZimChecked")
    @ApiModelProperty("로그인한 사용자의 찜 여부") protected boolean isZimChecked;

    @ApiModelProperty("찜 갯수") protected int zim;
    @ApiModelProperty("소개글") protected String introduce;

    @ApiModelProperty("가게") protected StoreDto store;
}
