package com.sej.escape.dto.theme;

import com.sej.escape.dto.store.StoreNameDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeNameDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("테마명") private String name;

    private StoreNameDto store;
}