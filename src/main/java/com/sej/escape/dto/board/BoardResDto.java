package com.sej.escape.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResDto {

    private long id;
    private long randomId;
    private String title;
    private String writer;
    private LocalDateTime regDate;
}
