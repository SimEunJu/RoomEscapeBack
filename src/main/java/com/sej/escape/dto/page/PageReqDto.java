package com.sej.escape.dto.page;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
public class PageReqDto {

    protected int page;
    protected int size;
    protected String searchKeyword;

    public PageReqDto(){
        this.page = 1;
        this.size = 10;
    }

    public void setPage(int page) {
        if(page <= 0){
            this.page = 1;
            return;
        }
        this.page = page;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }

    public PageRequest getPageable(){
        return PageRequest.of(page-1, size);
    }
}