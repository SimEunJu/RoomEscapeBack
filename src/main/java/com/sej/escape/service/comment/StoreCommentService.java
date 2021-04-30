package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
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

    private final StoreCommentRepository stotreCommentRepository;
    private final ThemeCommentRepository themeCommentRepository;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;
    private final ModelMapper modelMapper;

    public CommentResDto addComment(CommentModifyReqDto commentModifyReqDto){
        StoreComment storeComment = commentMapper.mapReqDtoToStoreComment(commentModifyReqDto);
        stotreCommentRepository.save(storeComment);
        return commentMapper.mapEntityToDto(storeComment, CommentResDto.class);
    }

    public CommentListResDto getComments(CommentListReqDto reqDto){

        Sort sort = reqDto.getOrder().getSort();
        Pageable pageable = reqDto.getPageable(sort);
        Member member = authenticationUtil.getAuthUserEntity();

        Page<Object[]> commentPage = stotreCommentRepository.findAllByMember(pageable, member);
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
        return entitis.stream().map(e -> {

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
        }).collect(Collectors.toList());
    }
}
