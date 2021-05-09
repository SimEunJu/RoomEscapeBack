package com.sej.escape.dto.board;

import com.sej.escape.constants.BoardType;
import com.sej.escape.dto.page.PageReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardReqDto extends PageReqDto {

    private BoardType type;
}
