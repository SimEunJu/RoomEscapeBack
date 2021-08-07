package com.sej.escape.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDeleteResDto {

    @ApiModelProperty("게시글 아이디") private long id;
    @ApiModelProperty("게시글 아이디 리스트") private List<Long> ids;

    @ApiModelProperty("게시판 타입") private String type;
}
