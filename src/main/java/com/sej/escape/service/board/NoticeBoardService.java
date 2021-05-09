package com.sej.escape.service.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.board.BoardReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.board.*;
import com.sej.escape.repository.board.NoticeBoardRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService implements IBoardService{

    private final NoticeBoardRepository noticeBoardRepository;
    private final AuthenticationUtil authenticationUtil;
    private final BoardMapper mapper;

    @Override
    public PageResDto getBoards(BoardReqDto reqDto) {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = reqDto.getPageable(sort);
        QNoticeBoard qBoard = QNoticeBoard.noticeBoard;

        BooleanBuilder builder = new BooleanBuilder(qBoard.isDeleted.isFalse());
        if(StringUtils.hasText(reqDto.getSearchKeyword())){
            builder.and(qBoard.title.contains(reqDto.getSearchKeyword()));
        }
        Page<NoticeBoard> boardPage = noticeBoardRepository.findAll(builder, pageable);
        return new PageResDto<>(boardPage,
                (NoticeBoard board) -> mapper.mapBoardToDto(board, BoardDto.class) );
    }


    @Override
    public Board addBoard(BoardDto dto) {
        NoticeBoard board = mapper.map(dto, NoticeBoard.class);
        Member member = authenticationUtil.getAuthUserEntity();
        board.setMember(member);
        return noticeBoardRepository.save(board);
    }


}
