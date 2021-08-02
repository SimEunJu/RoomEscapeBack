package com.sej.escape.dto.board;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.file.FileResDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    @ApiModelProperty("아이디") long id;
    @ApiModelProperty("게시글 동록 시 사용되는 랜덤 아이디") private long randomId;

    @ApiModelProperty("작성자명") private String writer;
    @ApiModelProperty("작성자 아이디") private Long writerId;

    @ApiModelProperty("제목") private String title;
    @ApiModelProperty("등록날짜") private LocalDateTime regDate;
    @ApiModelProperty("내용") private String content;
    @ApiModelProperty("조회수") private int view;

    @ApiModelProperty("업로드 파일") private FileResDto[] uploadFiles;

    @ApiModelProperty("게시판 종류") private BoardType type;

}
