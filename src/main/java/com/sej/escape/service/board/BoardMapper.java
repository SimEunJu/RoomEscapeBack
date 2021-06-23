package com.sej.escape.service.board;

import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.entity.board.Board;
import com.sej.escape.entity.board.ReqBoard;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class BoardMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void afterConstruct(){
        this.modelMapper.createTypeMap(Board.class, BoardDto.class)
                .addMapping(src -> src.getMember().getNickname(), BoardDto::setWriter);
    }

    public <D, E> E mapDtoToEntity(D dto, Class<E> entity){
        return modelMapper.map(dto, entity);
    }
    public <E, D> D mapEntityToDto(E entity, Class<D> dest){
        return modelMapper.map(entity, dest);
    }
}
