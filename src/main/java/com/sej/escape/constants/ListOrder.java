package com.sej.escape.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
