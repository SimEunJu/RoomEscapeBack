package com.sej.escape.dto.page;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class PageResDto<Dto, En> {

    private List<Dto> dtoList;

    private int totalPages;
    private int page;
    private int size;
    private boolean hasNext;

    public PageResDto(Page<En> result, Function<En, Dto> fn){
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
        this.totalPages = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        this.hasNext = page < totalPages;

    }

}
