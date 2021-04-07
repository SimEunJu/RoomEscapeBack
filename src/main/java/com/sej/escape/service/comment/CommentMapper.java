package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.entity.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ModelMapper modelMapper;

    public <T> List<CommentDto> mapEntitesToDtos(List<T> entities){
        return entities.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    public <T extends Comment> CommentDto mapEntityToDto(T entity){
        return modelMapper.map(entity, CommentDto.class);
    }
}
