package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageResDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResDto extends PageResDto {

    @ApiModelProperty("종류") private String type;
    @ApiModelProperty("댓글이 사용되는 도메인") private Ancestor ancestor;
    @ApiModelProperty("대댓글 여부") private boolean hasRecomment;

}
