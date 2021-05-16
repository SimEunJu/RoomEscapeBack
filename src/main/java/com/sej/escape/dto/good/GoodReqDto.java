package com.sej.escape.dto.good;

import com.sej.escape.constants.GoodType;
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
    @NotNull private GoodType type;
    @NotNull private Boolean isChecked;

}
