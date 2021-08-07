package com.sej.escape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyResDto {

    private long reqCnt;
    private long modifyCnt;
    private boolean isAllModified;
}
