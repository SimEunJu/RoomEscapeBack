package com.sej.escape.dto.comment;

import com.sej.escape.error.ErrorRes;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResDto extends CommentDto{

    private String type;

    private long randId;
    private boolean hasError;
    private ErrorRes error;

    @Builder(builderMethodName = "resBuilder")
    public CommentResDto(long id, String content, int depth, int seq, int good, boolean isGoodChecked, double star, LocalDateTime regDate, LocalDateTime updateDate, LocalDateTime deleteDate, String type, long randId, boolean hasError, ErrorRes error) {
        super(id, content, depth, seq, good, isGoodChecked, star, regDate, updateDate, deleteDate);
        this.type = type;
        this.randId = randId;
        this.hasError = hasError;
        this.error = error;
    }
}
