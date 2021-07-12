package com.sej.escape.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ThemeCommentForListByMemberDto {

    protected long id;
    protected long themeId;

    protected String name;
    protected LocalDateTime visitDate;
    protected LocalDateTime regDate;
    protected int star;
    protected int themeCnt;
    protected int visitThemeCnt;

    @JsonProperty(value="isHidden")
    protected boolean isHidden;
    protected String writer;

}
