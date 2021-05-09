package com.sej.escape.dto.board;

import com.sej.escape.constants.BoardType;
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
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private String content;
    private FileResDto[] uploadFiles;
    private BoardType type;

}
