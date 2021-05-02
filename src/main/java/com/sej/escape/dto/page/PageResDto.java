package com.sej.escape.dto.page;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class PageResDto<En, Dto> {

    private List<Dto> targetList;

    private int total;
    private int page;
    private int size;
    private boolean hasNext;

    public PageResDto(Page<En> result, Function<En, Dto> fn){
        this.targetList = result.stream().map(fn).collect(Collectors.toList());
        this.total = result.getTotalPages();
        this.page = result.getNumber() + 1;
        this.size = result.getSize();
        this.hasNext = result.hasNext();
    }
}
