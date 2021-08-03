package com.sej.escape.dto.comment.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.comment.Ancestor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreCommentDto {

    //private String ancestorType;
    @ApiModelProperty("댓글 사용하는 도메인") private Ancestor ancestor;

    @ApiModelProperty("댓글 등록 시 사용하는 랜덤 아이디") private long randId;

    // store
    @ApiModelProperty("가게 아이디") private long storeId;
    @ApiModelProperty("가게명") private String name;

    // comment
    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("등록날짜") private LocalDateTime regDate;
    @ApiModelProperty("별점") private int star;
    @JsonProperty("isHidden")
    @ApiModelProperty("숨김 여부") private boolean isHidden;

    // theme
    @ApiModelProperty("하위의 테마 갯수") private int themeCnt;
    @ApiModelProperty("로그인한 사용하자 방문한 하위 테마 갯수") private int visitThemeCnt;
}
