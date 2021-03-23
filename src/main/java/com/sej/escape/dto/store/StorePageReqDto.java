package com.sej.escape.dto.store;

import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.ListOrder;
import com.sej.escape.dto.page.PageReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StorePageReqDto extends PageReqDto {

    private ListOrder order;

    private AreaSection[] selectedAreaSection;

    private double latitude;
    private double longitude;
}
