package com.sej.escape.constants.dto;

public enum BoardType {
    NOTICE, REQ;

    public String getTypeString(){
        return this.toString().toLowerCase();
    }
}
