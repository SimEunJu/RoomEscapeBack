package com.sej.escape.dto.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.store.StoreDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeZimListResDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("찜 아이디") private long zimId;

    @ApiModelProperty("테마명") private String name;
    @ApiModelProperty("대표 이미지 url") private String imgUrl;

    @JsonProperty("isZimChecked")
    @ApiModelProperty("로그인 사용자 찜 여부") private boolean isZimChecked;

    @ApiModelProperty("가게명") private String storeName;
}
