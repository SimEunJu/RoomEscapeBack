package com.sej.escape.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreCommentDto {

    private String name;
    private LocalDateTime visitDate;
    private int star;
    private int themeCnt;
    private int visitThemeCnt;
    private boolean isHidden;

}
