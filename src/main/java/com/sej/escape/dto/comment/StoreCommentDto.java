package com.sej.escape.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreCommentDto {

    // store
    private long storeId;
    private String name;

    // comment
    private long id;
    private LocalDateTime regDate;
    private int star;
    @JsonProperty("isHidden")
    private boolean isHidden;

    // theme
    private int themeCnt;
    private int visitThemeCnt;
}
