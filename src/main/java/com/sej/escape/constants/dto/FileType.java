package com.sej.escape.constants.dto;

public enum FileType {
    BOARD, THEMECOMMENT;

    public String getTypeString(){
        return this.toString().toLowerCase();
    }
}
