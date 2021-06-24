package com.sej.escape.dto.theme;

import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.dto.ListOrder;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.constants.Genre;
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

    // TODO: enum으로 관리
    //private String type;
    private String searchKeyword;

    private ListOrder order = ListOrder.DEFAULT;

    private AreaSection[] areaSection;

    private double latitude;
    private double longitude;

    private Genre[] genre;

    @Min(1)
    @Max(5)
    private int difficulty;
}
