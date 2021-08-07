package com.sej.escape.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.entity.geolocation.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreDto {

    @ApiModelProperty("아이디") long id;
    @ApiModelProperty("주소") private Address address;
    @ApiModelProperty("지역(계층화)") private List<String> area;
    @ApiModelProperty("가게명") private String name;
    @ApiModelProperty("가게 홈페이지 url") private String link;
    @ApiModelProperty("전화번호") private String tel;
    @ApiModelProperty("찜 갯수") private long zim;

    @JsonProperty("isZimChecked")
    @ApiModelProperty("로그인한 사용자의 찜 여부") private boolean isZimChecked;

    private double star;
    @ApiModelProperty("대표 이미지 url") private String imgUrl;
    @ApiModelProperty("소개") private String introduce;

    @ApiModelProperty("별점 평균")
    public double getStar(){
        return Math.round(star*100) / 100;
    }
}
