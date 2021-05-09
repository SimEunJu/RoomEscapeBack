package com.sej.escape.service.board;

import com.querydsl.core.types.Predicate;
import com.sej.escape.dto.board.BoardDto;

import com.sej.escape.dto.board.BoardReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.entity.board.Board;
import org.springframework.data.domain.Pageable;

public interface IBoardService {
    PageResDto getBoards(BoardReqDto reqDto);
    Board addBoard(BoardDto dto);

}
