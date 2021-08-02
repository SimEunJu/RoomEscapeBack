package com.sej.escape.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreZimListResDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("찜 아이디") private long zimId;

    @ApiModelProperty("가게명") private String name;
    @ApiModelProperty("대표 이미지 url") private String imgUrl;

    @JsonProperty("isZimChecked")
    @ApiModelProperty("찜 여부") private boolean isZimChecked;
}
