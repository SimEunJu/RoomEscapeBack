package com.sej.escape.dto.board;

import com.sej.escape.dto.file.FileResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDto {

    private long id;
    private String writer;
    private String title;
    private LocalDateTime regDate;
    private String content;
    
    // TODO: 배열 1개로 관리 vs. 나누냐
    private FileResDto[] files;
    private FileResDto file;
}
