package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.dto.comment.StoreCommentDto;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.asm.Type;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentMapper {

    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;

    public <T, D> List<D> mapEntitesToDtos(List<T> entities, Class<D> dest){
        return entities.stream()
                .map(comment -> modelMapper.map(comment, dest))
                .collect(Collectors.toList());
    }

    public <E extends Comment, D> D mapEntityToDto(E entity, Class<D> dest){
        return modelMapper.map(entity, dest);
    }

    private <T extends Comment> T mapReqDtoToComment(Class<T> entityCls, CommentModifyReqDto commentModifyReqDto)  {

        CommentDto parComment = commentModifyReqDto.getParComment();
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
        entity.setReferId(commentModifyReqDto.getAncestor().getId());
        entity.setContent(commentModifyReqDto.getContent());
        entity.setParId(parId);
        entity.setDepth(depth);
        entity.setSeq(seq);

        return entity;
    }

    public BoardComment mapReqDtoToBoardComment(CommentModifyReqDto commentModifyReqDto) {
        return mapReqDtoToComment(BoardComment.class, commentModifyReqDto);
    }

    public StoreComment mapReqDtoToStoreComment(CommentModifyReqDto commentModifyReqDto) {
        StoreComment storeComment = mapReqDtoToComment(StoreComment.class, commentModifyReqDto);
        storeComment.setStar(commentModifyReqDto.getStarRate());
        return storeComment;
    }

    public List<StoreCommentDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(e -> {
            StoreComment storeComment = (StoreComment) e[0];
            Store store = (Store) e[1];
            StoreCommentDto dto = modelMapper.map(storeComment, StoreCommentDto.class);
            dto.setName(store.getStoreName());
            return dto;
        }).collect(Collectors.toList());
    }
}
