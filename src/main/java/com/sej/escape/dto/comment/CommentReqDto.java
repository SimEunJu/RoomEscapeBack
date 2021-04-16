package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReqDto extends PageReqDto {

    private Long id;
    @NotNull private Long referId;
    @NotEmpty private String type;

    @Builder(builderMethodName = "reqBuilder")
    public CommentReqDto(int page, int size, String searchKeyword, Long id, @NotNull Long referId, @NotEmpty String type) {
        super(page, size, searchKeyword);
        this.id = id;
        this.referId = referId;
        this.type = type;
    }
}
