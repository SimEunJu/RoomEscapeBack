package com.sej.escape.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListOrder {

    LATEST("최신 등록"),
    GOOD("별점"),
    ZIM("찜 갯수"),
    CLOSEST("가까운 거리"),
    DEFAULT(null)
    ;

    private String title;

}
