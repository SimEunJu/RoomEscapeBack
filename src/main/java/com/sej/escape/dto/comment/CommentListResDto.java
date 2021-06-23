package com.sej.escape.dto.comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResDto {

    private String type;
    private Ancestor ancestor;
    private boolean hasRecomment;

    private long total;
    private int page;
    private int size;
    private boolean hasNext;
    private List<?> comments;
}
