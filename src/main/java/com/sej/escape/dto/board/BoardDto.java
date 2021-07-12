package com.sej.escape.dto.board;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.file.FileResDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private long id;
    private long randomId;

    private String writer;
    private Long writerId;

    private String title;
    private LocalDateTime regDate;
    private String content;
    private int view;

    private FileResDto[] uploadFiles;

    private BoardType type;

}
