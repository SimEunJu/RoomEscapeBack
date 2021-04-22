package com.sej.escape.dto.zim;

import com.sej.escape.constants.ZimType;
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
    @NotNull private ZimType type;
    @NotNull private Boolean isChecked;

}
