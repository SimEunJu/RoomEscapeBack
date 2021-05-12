package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.StoreCommentRepository;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository storeCommentRepository;
    private final ThemeCommentRepository themeCommentRepository;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;
    private final ModelMapper modelMapper;

    public CommentResDto addComment(CommentModifyReqDto reqDto){
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
        return CommentListResDto.builder()
                .total(commentPage.getTotalElements())
                .comments(mapStoreCommentsToDtos(storeComments))
                .size(commentPage.getSize())
                .hasNext(commentPage.hasNext())
                .page(reqDto.getPage())
                .build();
    }

    private List<StoreCommentDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(this::mapStoreCommentToDto).collect(Collectors.toList());
    }

    private StoreCommentDto mapStoreCommentToDto(Object[] e){
        StoreComment storeComment = (StoreComment) e[0];
        Store store = (Store) e[1];

        StoreCommentDto dto = modelMapper.map(storeComment, StoreCommentDto.class);
        dto.setName(store.getStoreName());
        dto.setStoreId(store.getId());

        Object[] themeCnt = (Object[]) themeCommentRepository.findThemeCntAndCommentCnt(store);
        long themeTot = (long) themeCnt[0];
        long themeVisitedCnt = (long) themeCnt[1];
        dto.setThemeCnt((int) themeTot);
        dto.setVisitThemeCnt((int) themeVisitedCnt);
        return dto;
    }
}
