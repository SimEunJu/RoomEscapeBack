package com.sej.escape.dto.theme;

import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.dto.ListOrder;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.constants.Genre;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ThemePageReqDto extends PageReqDto {

    @ApiModelProperty("정렬") private ListOrder order = ListOrder.DEFAULT;

    @ApiModelProperty("지역") private AreaSection[] areaSection;

    @ApiModelProperty("위도") private double latitude;
    @ApiModelProperty("경도") private double longitude;

    @ApiModelProperty("장르") private Genre[] genre;

    @Min(1)
    @Max(5)
    @ApiModelProperty("난이도") private int difficulty;
}
