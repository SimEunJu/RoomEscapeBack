package com.sej.escape.dto.comment;

import com.sej.escape.constants.CommentListOrder;
import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListReqDto extends PageReqDto {

    private CommentListOrder order;
}
