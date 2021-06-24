package com.sej.escape.dto.comment;

import com.sej.escape.constants.dto.CommentType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModifyReqDto {

    private String content;
    private double starRate;
    private long id;
    private long randId;
    private boolean isGood;
    private boolean isHidden;

    private CommentType type;

    private CommentDto parComment;
    private Ancestor ancestor;
}
