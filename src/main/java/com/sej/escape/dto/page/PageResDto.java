package com.sej.escape.dto.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class PageResDto<En, Dto> {

    private List<Dto> targetList;
    private String type;

    private int total;
    private int page;
    private int size;
    private boolean hasNext;

    private void setPageResult(Page<En> result){
        this.total = result.getTotalPages();
        this.page = result.getNumber() + 1;
        this.size = result.getSize();
        this.hasNext = result.hasNext();
    }

    public PageResDto(Page<En> result, Function<En, Dto> fn){
        this.targetList = result.stream().map(fn).collect(Collectors.toList());
        setPageResult(result);
    }

}
