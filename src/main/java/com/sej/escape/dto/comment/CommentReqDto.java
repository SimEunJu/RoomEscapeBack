package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqDto extends PageReqDto {

    private String content;
    private double starRate;
    private long id;
    private long randId;
    private CommentDto parComment;
    private Ancestor ancestor;

}
