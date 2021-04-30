package com.sej.escape.constants;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
public enum CommentListOrder {
    LATEST("regDate"), STAR("star");

    private String column;

    public Sort getSort(){
        return Sort.by(Sort.Direction.DESC, this.column);
    }
}
