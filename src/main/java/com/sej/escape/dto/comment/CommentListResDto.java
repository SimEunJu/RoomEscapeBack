package com.sej.escape.dto.comment;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentListResDto {

    private long total;
    private int page;
    private int size;
    private boolean hasNext;
    private List<StoreCommentDto> comments;
}