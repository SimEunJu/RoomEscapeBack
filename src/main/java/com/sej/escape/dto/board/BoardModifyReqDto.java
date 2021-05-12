package com.sej.escape.dto.board;

import com.sej.escape.constants.BoardType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardModifyReqDto{

    private List<Long> ids;
    private BoardType type;

}
