package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class CommentReqDto  extends PageReqDto {

    private String content;
    private double starRate;
    private long id;
    private long randId;
    private CommentDto parComment;
    private Ancestor ancestor;

}
