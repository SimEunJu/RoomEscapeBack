package com.sej.escape.constants.dto;

public enum ZimType {
    STORE, THEME;

    public String getTypeString(){
        return this.toString().toLowerCase();
    }
}
