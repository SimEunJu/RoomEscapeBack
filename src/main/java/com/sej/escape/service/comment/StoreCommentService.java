package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.comment.store.StoreCommentDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.error.exception.AlreadyExistResourceException;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.StoreCommentRepository;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository storeCommentRepository;
    private final ThemeCommentRepository themeCommentRepository;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;

    private void checkAlreadyExist(CommentModifyReqDto reqDto){

        Member member = authenticationUtil.getAuthUserEntity();

        Optional<StoreComment> storeCommentExist = null;
        try{
            storeCommentExist = storeCommentRepository.findByMemberAndIsDeletedFalse(member);

        }catch (NonUniqueResultException e){
            throwAlreadyExistException(reqDto.getAncestor().getId());
        }
        storeCommentExist.ifPresent(
                (StoreComment comment) -> throwAlreadyExistException(reqDto.getAncestor().getId())
        );
    }

    private void throwAlreadyExistException(long storeId){
        throw new AlreadyExistResourceException(
                String.format("가게 아이디 [%d]에 대한 후기가 이미 존재합니다.", storeId));
    }

    public CommentResDto addComment(CommentModifyReqDto reqDto){

        checkAlreadyExist(reqDto);

        StoreComment storeComment = saveComment(reqDto);
        if(reqDto.getParComment() == null) {
            storeComment.setParId(storeComment.getId());

        }else{
            StoreComment parComment = storeCommentRepository.findById(reqDto.getParComment().getId())
                    .orElseThrow(
                            () -> new NoSuchResourceException(
                                    String.format("[%l]에 해당하는 Comment가 없습니다.",reqDto.getId() ))
                    );
            storeComment.setParId(parComment.getParId());
        }

        storeCommentRepository.save(storeComment);

        return commentMapper.mapEntityToDto(storeComment, CommentResDto.class);
    }

    private StoreComment saveComment(CommentModifyReqDto commentModifyReqDto){
        StoreComment storeComment = commentMapper.mapReqDtoToEntity(commentModifyReqDto, StoreComment.class);

        return storeCommentRepository.save(storeComment);
    }

    public StoreCommentDto addCommentAndRetDetail(CommentModifyReqDto commentModifyReqDto){
        StoreComment storeComment = saveComment(commentModifyReqDto);

        Member member = authenticationUtil.getAuthUserEntity();

        Object[] comment = (Object[]) storeCommentRepository.findByIdAndMember(member, storeComment.getId());

        StoreCommentDto commentDto = mapStoreCommentToDto(comment);

        return commentDto;
    }

    public CommentListResDto getCommentsByMember(CommentListReqDto reqDto){

        Sort sort = reqDto.getOrder().getSort();
        Pageable pageable = reqDto.getPageable(sort);
        Member member = authenticationUtil.getAuthUserEntity();

        Page<Object[]> commentPage = storeCommentRepository.findAllByMember(pageable, member);
        List<Object[]> storeComments = commentPage.getContent();

        CommentListResDto resDto = new CommentListResDto();
        resDto.setPageResult(commentPage);
        resDto.setTargetList(mapStoreCommentsToDtos(storeComments));

        return resDto;
    }

    private List<StoreCommentDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(this::mapStoreCommentToDto).collect(Collectors.toList());
    }

    private StoreCommentDto mapStoreCommentToDto(Object[] e){
        StoreComment storeComment = (StoreComment) e[0];
        Store store = (Store) e[1];

        StoreCommentDto dto = commentMapper.mapEntityToDto(storeComment, StoreCommentDto.class);
        dto.setName(store.getName());
        dto.setStoreId(store.getId());

        Member writer = storeComment.getMember();

        Object[] themeCnt = (Object[]) themeCommentRepository.findThemeCntAndCommentCnt(store, writer);

        long themeTot = (long) themeCnt[0];
        long themeVisitedCnt = (long) themeCnt[1];
        dto.setThemeCnt((int) themeTot);
        dto.setVisitThemeCnt((int) themeVisitedCnt);

        return dto;
    }
}
