package com.sej.escape.dto.good;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
public class GoodReqDto {

    private Long id;
    @NotNull private Long referId;
    @NotEmpty private String type;
    @NotNull private Boolean isChecked;

}
