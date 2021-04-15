package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder(builderMethodName = "commentReqDtoBuilder")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReqDto extends PageReqDto {

    private Long id;
    @NotNull private Long referId;
    @NotEmpty private String type;

}
