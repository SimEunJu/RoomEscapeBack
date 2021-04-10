package com.sej.escape.dto.comment;

import com.sej.escape.error.ErrorRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class CommentResDto {

    private String type;
    private long id;
    private long randId;
    private boolean hasError;
    private ErrorRes error;

}
