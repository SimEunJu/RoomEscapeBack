package com.sej.escape.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeCommentForListDto {

    private long id;
    private String content;
    private String writer;

    private int good;
    @JsonProperty("isGoodChecked")
    private boolean isGoodChecked;
    private double star;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

}
