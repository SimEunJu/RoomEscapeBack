package com.sej.escape.dto.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreNameDto {

    @ApiModelProperty("아이디") protected long id;
    @ApiModelProperty("가게명") protected String name;
}
