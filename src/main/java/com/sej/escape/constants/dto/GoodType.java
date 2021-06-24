package com.sej.escape.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoodType {
    STORE("S"),
    THEME("T"),
    THEME_COMMENT("CT"), STORE_COMMENT("CS"),
    BOARD_REQ_COMMENT("CBR"), BOARD_NOTICE_COMMENT("CBR");

    // to: 내부 entity
    private String entityDiscriminatorValue;
    public String getTypeString(){
        return this.toString().toLowerCase();
    }
}
