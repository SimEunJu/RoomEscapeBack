package com.sej.escape.dto.comment;

import com.sej.escape.error.ErrorRes;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeCommentResDto extends CommentDto{

    private String type;

    private long randId;
    private boolean hasError;
    private ErrorRes error;

    @Builder(builderMethodName = "resBuilder")
    public ThemeCommentResDto(long id, String content, String writer, long writerId, long parId, int depth, int seq, int good, boolean isGoodChecked, boolean isDeleted, double star, boolean isHidden, LocalDateTime regDate, LocalDateTime updateDate, LocalDateTime deleteDate, String type, long randId, boolean hasError, ErrorRes error) {
        super(id, content, writer, writerId, parId, depth, seq, good, isGoodChecked, isDeleted, star, isHidden, regDate, updateDate, deleteDate);
        this.type = type;
        this.randId = randId;
        this.hasError = hasError;
        this.error = error;
    }
}