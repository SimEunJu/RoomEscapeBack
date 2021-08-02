package com.sej.escape.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
public class PageReqDto {

    @ApiModelProperty("페이지") protected int page;
    @ApiModelProperty("페이지 크기") protected int size;
    @ApiModelProperty("검색어") protected String searchKeyword;

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

    public int getPage(){
        return this.page - 1;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }

    public PageRequest getPageable(){
        return PageRequest.of(page-1, size);
    }
}