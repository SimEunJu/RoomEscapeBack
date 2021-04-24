package com.sej.escape.service;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.board.BoardResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.entity.Board;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.QBoard;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.BoardRepository;
import com.sej.escape.service.file.FileService;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final AuthenticationUtil authenticationUtil;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void afterConstruct(){
        this.modelMapper.createTypeMap(Board.class, BoardDto.class)
                .addMapping(src -> src.getMember().getNickname(), BoardDto::setWriter);
    }

    public PageResDto<Board, BoardDto> getBoards(PageReqDto pageReqDto){
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);
        QBoard qBoard = QBoard.board;
        BooleanBuilder builder = new BooleanBuilder(qBoard.title.contains(pageReqDto.getSearchKeyword()));
        Page<Board> boards = boardRepository.findAllByIsDeletedFalse(builder, pageable);
        return new PageResDto<>(boards, this::mapBoardToDto);
    }

    public BoardDto getBoard(long id){
        Optional<Board> boardOpt = boardRepository.findByIdAndIsDeletedFalse(id);
        Board board = getBoardIfExist(boardOpt, id);
        return mapBoardToDto(board);
    }

    public long deleteBoards(List<Long> ids){
        long deleteCnt = boardRepository.updateDeleteTAllByIdIn(ids);
        return deleteCnt;
    }

    public BoardDto updateBoard(BoardDto boardDto) {
        Optional<Board> boardOpt = boardRepository.findByIdAndIsDeletedFalse(boardDto.getId());
        Board board = getBoardIfExist(boardOpt, boardDto.getId());
        board.setContent(boardDto.getContent());
        board.setTitle(boardDto.getTitle());
        Board boardUp = boardRepository.save(board);
        return mapBoardToDto(boardUp);
    }

    public BoardResDto addBoard(BoardDto boardDto){
        Board board = modelMapper.map(boardDto, Board.class);
        Member member = authenticationUtil.getAuthUserEntity();
        board.setMember(member);
        board = boardRepository.save(board);

        if(boardDto.getUploadFiles().length > 0){
            List<Long> ids = Arrays.stream(boardDto.getUploadFiles()).map(file -> file.getId()).collect(Collectors.toList());
            fileService.updateReferIds(ids, board.getId());
        }

        BoardResDto resDto = modelMapper.map(boardDto, BoardResDto.class);
        resDto.setId(board.getId());
        resDto.setRegDate(board.getRegDate());
        resDto.setWriter(member.getNickname());
        return resDto;
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
