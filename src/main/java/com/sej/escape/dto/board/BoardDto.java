package com.sej.escape.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
    private long id;
    private String writer;
    private String title;
    private String regDate;
    private String content;
}
