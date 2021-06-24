package com.sej.escape.dto.zim;

import com.sej.escape.constants.dto.ZimType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZimReqDto {

    private Long id;
    @NotNull private Long referId;
    @NotNull private ZimType type;
    @NotNull private Boolean isChecked;

}
