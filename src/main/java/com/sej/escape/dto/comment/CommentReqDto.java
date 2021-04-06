package com.sej.escape.dto.comment;

import com.sej.escape.dto.page.PageReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class CommentReqDto  extends PageReqDto {

    private long id;

}
