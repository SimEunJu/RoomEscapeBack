package com.sej.escape.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListOrder {

    LATEST("최신순"),
    GOOD("좋아요순"),
    ZIM("찜순"),
    CLOSEST("가까운순")
    ;

    private String title;

}
