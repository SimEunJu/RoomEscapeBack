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

    // TODO: enum으로 관리
    private String type;
    private String searchKeyword;

    private ListOrder order = ListOrder.DEFAULT;

    private AreaSection[] areaSection;

    private double latitude;
    private double longitude;

}
