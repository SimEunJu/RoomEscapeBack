package com.sej.escape.service.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.board.BoardReqDto;
import com.sej.escape.dto.board.BoardResDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.board.*;
import com.sej.escape.repository.board.ReqBoardRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReqBoardService implements IBoardService{

    private final ReqBoardRepository reqBoardRepository;
    private final AuthenticationUtil authenticationUtil;
    private final BoardMapper mapper;

    @Override
    public PageResDto getBoards(BoardReqDto reqDto) {
        // TODO: qBoard 부분만 빼고 안 겹치는 데 어떻게 하나.
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = reqDto.getPageable(sort);
        QReqBoard qBoard = QReqBoard.reqBoard;

        BooleanBuilder builder = new BooleanBuilder(qBoard.isDeleted.isFalse());
        if(StringUtils.hasText(reqDto.getSearchKeyword())){
            builder.and(qBoard.title.contains(reqDto.getSearchKeyword()));
        }
        Page<ReqBoard> boardPage = reqBoardRepository.findAll(builder, pageable);
        return new PageResDto<>(boardPage,
                (ReqBoard board) -> mapper.mapBoardToDto(board, BoardDto.class) );
    }

    @Override
    public Board addBoard(BoardDto dto) {
        ReqBoard board = mapper.map(dto, ReqBoard.class);
        Member member = authenticationUtil.getAuthUserEntity();
        board.setMember(member);
        return reqBoardRepository.save(board);
    }

}
