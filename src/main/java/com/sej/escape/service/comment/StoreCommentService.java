package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.repository.comment.StoreCommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository stotreCommentRepository;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;

    public CommentResDto addComment(CommentModifyReqDto commentModifyReqDto){
        StoreComment storeComment = commentMapper.mapReqDtoToStoreComment(commentModifyReqDto);
        stotreCommentRepository.save(storeComment);
        return commentMapper.mapEntityToDto(storeComment, CommentResDto.class);
    }

    public List<StoreCommentDto> getComments(PageReqDto pageReqDto){
        Pageable pageable = pageReqDto.getPageable();
        Member member = authenticationUtil.getAuthUserEntity();
        List<Object[]> storeComments = stotreCommentRepository.findAllByMember(pageable, member);
        return commentMapper.mapStoreCommentsToDtos(storeComments);
    }

}
