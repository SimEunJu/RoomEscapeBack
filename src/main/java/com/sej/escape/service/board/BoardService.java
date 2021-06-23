package com.sej.escape.service.board;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.board.BoardReqDto;
import com.sej.escape.dto.board.BoardResDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.entity.board.Board;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.board.QBoard;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.board.BoardRepository;
import com.sej.escape.service.file.FileService;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final BoardMapper mapper;

    public PageResDto<Board, BoardDto> getBoards(BoardReqDto pageReqDto, IBoardService boardService){
        return boardService.getBoards(pageReqDto);
    }

    public BoardDto getBoard(long id){
        Optional<Board> boardOpt = boardRepository.findByIdAndIsDeletedFalse(id);
        Board board = getBoardIfExist(boardOpt, id);
        return mapper.mapEntityToDto(board, BoardDto.class);
    }

    public int deleteBoards(List<Long> ids){
        int deleteCnt = boardRepository.updateDeleteTAllByIdIn(ids);
        return deleteCnt;
    }

    public BoardDto updateBoard(BoardDto boardDto) {
        Optional<Board> boardOpt = boardRepository.findByIdAndIsDeletedFalse(boardDto.getId());
        Board board = getBoardIfExist(boardOpt, boardDto.getId());

        board.setContent(boardDto.getContent());
        board.setTitle(boardDto.getTitle());

        Board boardUp = boardRepository.save(board);
        return mapper.mapEntityToDto(boardUp, BoardDto.class);
    }

    public BoardResDto addBoard(BoardDto boardDto, IBoardService boardService){

        Board board = boardService.addBoard(boardDto);

        if(boardDto.getUploadFiles().length > 0){
            List<Long> ids = Arrays.stream(boardDto.getUploadFiles())
                    .map(file -> file.getId())
                    .collect(Collectors.toList());
            fileService.updateReferIds(ids, board.getId());
        }

        BoardResDto resDto = mapper.mapEntityToDto(board, BoardResDto.class);
        resDto.setWriter(board.getMember().getNickname());
        resDto.setRandomId(boardDto.getRandomId());
        resDto.setType(boardDto.getType().toString());

        return resDto;
    }

    private Board getBoardIfExist(Optional<Board> boardOpt, long id) {
        Board board = boardOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 게시글이 존재하지 않습니다.", id)));
        return board;
    }

}
