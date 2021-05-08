package com.sej.escape.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ThemeCommentForListDto {

    protected long id;
    protected long themeId;

    protected String name;
    protected LocalDateTime visitDate;
    protected int star;
    protected int themeCnt;
    protected int visitThemeCnt;
    protected boolean isHidden;

}
