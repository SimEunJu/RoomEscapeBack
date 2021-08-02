package com.sej.escape.dto.store;

import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.dto.ListOrder;
import com.sej.escape.dto.page.PageReqDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class StorePageReqDto extends PageReqDto {

    @ApiModelProperty("정렬")  private ListOrder order = ListOrder.DEFAULT;

    @ApiModelProperty("지역들") private AreaSection[] areaSection;

    @ApiModelProperty("위도")  private double latitude;
    @ApiModelProperty("경도") private double longitude;

}
