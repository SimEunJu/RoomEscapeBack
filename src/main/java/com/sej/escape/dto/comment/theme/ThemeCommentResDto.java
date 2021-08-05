package com.sej.escape.dto.comment.theme;

import com.sej.escape.dto.comment.Ancestor;
import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.error.ErrorRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeCommentResDto extends ThemeCommentDto {

    @ApiModelProperty("") private String type;
    @Builder.Default
    @ApiModelProperty("테마 리뷰를 사용하는 도메인") private Ancestor ancestor = Ancestor.builder().type("theme").build();

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("리뷰 생성 시 사용되는 랜덤 아이디") private long randId;
    @Builder.Default
    @ApiModelProperty("대댓글 여부") private boolean hasRecomment = false;
    
    @ApiModelProperty("") private boolean hasError;
    @ApiModelProperty("") private ErrorRes error;

    //@Builder(builderMethodName = "resBuilder")

}