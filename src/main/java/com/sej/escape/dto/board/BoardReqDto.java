package com.sej.escape.dto.board;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.page.PageReqDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardReqDto extends PageReqDto {

    @NonNull
    @ApiModelProperty("게시판 종류") private BoardType type;
}
