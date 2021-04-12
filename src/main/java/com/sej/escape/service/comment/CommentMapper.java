package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentMapper {

    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;

    public <T> List<CommentDto> mapEntitesToDtos(List<T> entities){
        return entities.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    public <T extends Comment> CommentDto mapEntityToDto(T entity){
        return modelMapper.map(entity, CommentDto.class);
    }

    private <T extends Comment> T mapReqDtoToComment(Class<T> entityCls, CommentReqDto commentReqDto)  {

        CommentDto parComment = commentReqDto.getParComment();
        boolean hasParComment = parComment != null;

        int depth = hasParComment ? parComment.getDepth()+1 : 0;
        int seq = hasParComment ? parComment.getSeq()+1 : 0;
        long parId = hasParComment ? parComment.getId() : 0;

        T entity = null;
        try {
            entity = entityCls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
            RuntimeException re = new RuntimeException(String.format("fail to create %s instance", entityCls.getName()));
            re.initCause(e);
            throw re;
        }

        entity.setMember(authenticationUtil.getAuthUserEntity());
        entity.setReferId(commentReqDto.getAncestor().getId());
        entity.setContent(commentReqDto.getContent());
        entity.setParId(parId);
        entity.setDepth(depth);
        entity.setSeq(seq);

        return entity;
    }

    public BoardComment mapReqDtoToBoardComment(CommentReqDto commentReqDto) {
        return mapReqDtoToComment(BoardComment.class, commentReqDto);
    }

    public StoreComment mapReqDtoToStoreComment(CommentReqDto commentReqDto) {
        StoreComment storeComment = mapReqDtoToComment(StoreComment.class, commentReqDto);
        storeComment.setStar(commentReqDto.getStarRate());
        return storeComment;
    }
}
