package com.sej.escape.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PageResDto<En, Dto> {

    @ApiModelProperty("리스트") private List<Dto> targetList;
    private String type;

    @ApiModelProperty("페이지") private int page;
    @ApiModelProperty("페이지 크기") private int size;
    @ApiModelProperty("총 페이지 수") private int total;
    @ApiModelProperty("다음 페이지 존재 여부") private boolean hasNext;

    public void setPageResult(Page<En> result) {
        this.total = result.getTotalPages();
        this.page = result.getNumber() + 1;
        //this.page = result.getNumber();
        this.size = result.getSize();
        this.hasNext = result.hasNext();
    }

    public PageResDto(Page<En> result, Function<En, Dto> fn){
        this.targetList = result.stream().map(fn).collect(Collectors.toList());
        setPageResult(result);
    }

}
