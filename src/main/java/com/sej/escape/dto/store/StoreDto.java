package com.sej.escape.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.entity.geolocation.Address;
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

    private long id;
    private Address address;
    private List<String> area;
    private String name;
    private String link;
    private String tel;
    private long zim;
    @JsonProperty("isZimChecked")
    private boolean isZimChecked;
    private double star;
    private String imgUrl;
    private String introduce;

    public double getStar(){
        return Math.round(star*100) / 100;
    }
}
