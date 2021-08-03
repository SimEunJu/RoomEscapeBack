package com.sej.escape.dto.comment.theme;

import com.sej.escape.dto.comment.Ancestor;
import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.error.ErrorRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeCommentResDto extends ThemeCommentDto {

    @ApiModelProperty("") private String type;
    @ApiModelProperty("") private Ancestor ancestor;

    @ApiModelProperty("") private long randId;
    @ApiModelProperty("") private boolean hasError;
    @ApiModelProperty("") private ErrorRes error;

    //@Builder(builderMethodName = "resBuilder")

}