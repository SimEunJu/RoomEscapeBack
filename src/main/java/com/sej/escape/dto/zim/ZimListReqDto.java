package com.sej.escape.dto.zim;

import com.sej.escape.constants.dto.ZimType;
import com.sej.escape.dto.page.PageReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZimListReqDto extends PageReqDto {

    private ZimType type;

    public String getType() {
        return type.name().toLowerCase();
    }
}
