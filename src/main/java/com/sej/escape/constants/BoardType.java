package com.sej.escape.constants;

public enum BoardType {
    NOTICE, REQ;

    public String getTypeString(){
        return this.toString().toLowerCase();
    }
}
