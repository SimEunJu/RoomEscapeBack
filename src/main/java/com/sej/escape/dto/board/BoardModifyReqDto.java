package com.sej.escape.dto.board;

import com.sej.escape.constants.dto.BoardType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardModifyReqDto{

    @ApiModelProperty("게시글 아이디 리스트") List<Long> ids;
    @ApiModelProperty("게시판 종류") private BoardType type;

}
