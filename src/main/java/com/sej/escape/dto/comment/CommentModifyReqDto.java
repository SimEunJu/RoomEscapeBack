package com.sej.escape.dto.comment;

import com.sej.escape.constants.dto.CommentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModifyReqDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("댓글 등록 시 사용되는 랜덤 아이디") private long randId;

    @ApiModelProperty("내용") private String content;
    @ApiModelProperty("별점") private double starRate;

    @ApiModelProperty("좋아요 여부") private boolean isGood;
    @ApiModelProperty("숨김 여부") private boolean isHidden;

    @ApiModelProperty("종류") private CommentType type;

    @ApiModelProperty("부모 댓글") private CommentDto parComment;
    @ApiModelProperty("댓글 사용하는 도메인") private Ancestor ancestor;

    public void setIsHidden(boolean isHidden){
        this.isHidden = isHidden;
    }
}
