package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

@Builder(builderMethodName = "commentModifyReqDtoBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentModifyReqDto extends PageReqDto {

    private String content;
    private double starRate;
    private long id;
    private long randId;
    private CommentDto parComment;
    private Ancestor ancestor;

}
