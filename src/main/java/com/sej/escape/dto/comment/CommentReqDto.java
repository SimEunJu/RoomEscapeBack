package com.sej.escape.dto.comment;

import com.sej.escape.constants.dto.CommentType;
import com.sej.escape.dto.page.PageReqDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReqDto extends PageReqDto {

    @ApiModelProperty("아이디") private Long id;

    @NotNull
    @ApiModelProperty("댓글 사용하는 도메인 아이디") private Long referId;

    @NotNull
    @ApiModelProperty("타입") private CommentType type;

    @Builder(builderMethodName = "reqBuilder")
    public CommentReqDto(int page, int size, String searchKeyword, Long id, @NotNull Long referId, @NotEmpty CommentType type) {
        super(page, size, searchKeyword);
        this.id = id;
        this.referId = referId;
        this.type = type;
    }
}
