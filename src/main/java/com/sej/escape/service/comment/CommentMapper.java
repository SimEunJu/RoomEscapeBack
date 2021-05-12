package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.entity.comment.NoticeBoardComment;
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

    public <D, E> E mapDtoToEntity(D dto, Class<E> entity){
        return modelMapper.map(dto, entity);
    }

    public <T, D> List<D> mapEntitesToDtos(List<T> entities, Class<D> dest){
        return entities.stream()
                .map(comment -> modelMapper.map(comment, dest))
                .collect(Collectors.toList());
    }

    public <E, D> D mapEntityToDto(E entity, Class<D> dest){
        return modelMapper.map(entity, dest);
    }

    private <T extends Comment> T mapReqDtoToComment(CommentModifyReqDto commentModifyReqDto, Class<T> entityCls)  {

        CommentDto parComment = commentModifyReqDto.getParComment();
        boolean hasParComment = parComment != null;

        int depth = hasParComment ? parComment.getDepth()+1 : 0;
        int seq = hasParComment ? parComment.getSeq()+1 : 0;
        long parId = hasParComment ? parComment.getParId() : 0;

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
        entity.setReferId(commentModifyReqDto.getAncestor().getId());
        entity.setContent(commentModifyReqDto.getContent());
        entity.setParId(parId);
        entity.setDepth(depth);
        entity.setSeq(seq);

        return entity;
    }

    public <E extends Comment> E mapReqDtoToEntity(CommentModifyReqDto commentModifyReqDto, Class<E> entity) {
        E comment = mapReqDtoToComment(commentModifyReqDto, entity);
        comment.setMember(authenticationUtil.getAuthUserEntity());
        comment.setReferId(commentModifyReqDto.getAncestor().getId());
        comment.setStar(commentModifyReqDto.getStarRate());
        return comment;
    }


}
