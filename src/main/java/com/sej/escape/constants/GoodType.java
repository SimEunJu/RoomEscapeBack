package com.sej.escape.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoodType {
    STORE("S"),
    THEME("T"),
    COMMENT_THEME("CT"), COMMENT_STORE("CS"),
    COMMENT_BOARD_REQ("CBR"), COMMENT_BOARD_NOTICE("CBR");

    // to: 내부 entity
    private String entityDiscriminatorValue;
}
