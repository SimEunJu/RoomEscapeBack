package com.sej.escape.dto.board;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResDto {

    private long id;
    private List<Long> ids;
    private long randomId;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    private String type;
    private List<String> subType;
}
