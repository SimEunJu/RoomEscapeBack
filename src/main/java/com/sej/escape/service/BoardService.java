package com.sej.escape.service;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.page.SearchReqDto;
import com.sej.escape.entity.Board;
import com.sej.escape.entity.QBoard;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private ModelMapper modelMapper;

    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper){
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(Board.class, BoardDto.class)
                .addMapping(src -> src.getMember().getMemberName(), BoardDto::setWriter);
    }

    public PageResDto<Board, BoardDto> getBoards(PageReqDto pageReqDto){
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);
        QBoard qBoard = QBoard.board;
        BooleanBuilder builder = new BooleanBuilder(qBoard.title.contains(pageReqDto.getSearchKeyword()));
        Page<Board> boards = boardRepository.findAllByDeletedNot(builder, pageable);
        return new PageResDto<>(boards, this::mapBoardToDto);
    }

    public BoardDto getBoard(long id){
        Optional<Board> boardOpt = boardRepository.findByIdByDeletedNot(id);
        Board board = getBoardIfExist(boardOpt, id);
        return mapBoardToDto(board);
    }

    public long deleteBoards(List<Long> ids){
        long deleteCnt = boardRepository.updateDeleteTAllByIdIn(ids);
        return deleteCnt;
    }

    public BoardDto updateBoard(BoardDto boardDto) {
        Optional<Board> boardOpt = boardRepository.findByIdByDeletedNot(boardDto.getId());
        Board board = getBoardIfExist(boardOpt, boardDto.getId());
        board.setContent(boardDto.getContent());
        board.setTitle(boardDto.getTitle());
        Board boardUp = boardRepository.save(board);
        return mapBoardToDto(boardUp);
    }

    public BoardDto addBoard(BoardDto boardDto){
        Board board = boardRepository.save(modelMapper.map(boardDto, Board.class));
        boardDto.setId(board.getId());
        boardDto.setRegDate(board.getRegDate());
        return boardDto;
    }

    private Board getBoardIfExist(Optional<Board> boardOpt, long id) {
        Board board = boardOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 게시글이 존재하지 않습니다.", id)));
        return board;
    }

    private BoardDto mapBoardToDto(Board board){
        return modelMapper.map(board, BoardDto.class);
    }

}
