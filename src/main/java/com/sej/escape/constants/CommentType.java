package com.sej.escape.constants;

import com.sej.escape.dto.comment.Ancestor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum CommentType {
    STORE("S", false),
    BOARD_REQ("BR", true), BOARD_NOTICE("BN", true);

    public Ancestor getAncestor(){
        String [] splitedTypes = this.toString().toLowerCase().split("_");
        List<String> types = Arrays.asList(splitedTypes);
        return Ancestor.builder()
                .type(types.get(0))
                .subTypes(types.subList(1, types.size()))
                .build();
    }
    private String discriminatorValue;
    // 대댓글 여부
    private boolean hasRecomment;

    public boolean hasRecomment() {
        return hasRecomment;
    }

    public void setRecomment(boolean hasRecomment) {
        this.hasRecomment = hasRecomment;
    }
}
