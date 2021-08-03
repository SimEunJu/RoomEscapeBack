package com.sej.escape.dto.comment.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ThemeCommentForListByMemberDto {

    @ApiModelProperty("아이디") protected long id;
    @ApiModelProperty("테마 아이디") protected long themeId;

    @ApiModelProperty("") protected String name;
    @ApiModelProperty("방문날짜") protected LocalDateTime visitDate;
    @ApiModelProperty("등록날짜") protected LocalDateTime regDate;
    @ApiModelProperty("별점") protected int star;
    @ApiModelProperty("같은 부모 하의 테마 수") protected int themeCnt;
    @ApiModelProperty("같은 부모 하의 방문 테마 수") protected int visitThemeCnt;

    @JsonProperty(value="isHidden")
    @ApiModelProperty("숨김 여부") protected boolean isHidden;
    @ApiModelProperty("작성자명")
    protected String writer;

}
