package com.sej.escape.dto.board;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.page.PageReqDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardReqDto extends PageReqDto {

    @NonNull private BoardType type;
}
