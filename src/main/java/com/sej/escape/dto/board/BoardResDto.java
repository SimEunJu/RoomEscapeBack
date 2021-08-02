package com.sej.escape.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResDto {

    @ApiModelProperty("게시글 아이디") private long id;
    @ApiModelProperty("게시글 생성 시 사용되는 랜덤 아이디") private long randomId;

    @ApiModelProperty("제목") private String title;
    @ApiModelProperty("작성자명") private String writer;
    @ApiModelProperty("등록일") private LocalDateTime regDate;

    @ApiModelProperty("게시판 타입") private String type;
    //private List<String> subType;
}
