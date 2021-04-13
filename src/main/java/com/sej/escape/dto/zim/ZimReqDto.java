package com.sej.escape.dto.zim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
public class ZimReqDto {

    private Long id;
    @NotNull private Long referId;
    @NotEmpty private String type;
    @NotNull private Boolean isChecked;

}
