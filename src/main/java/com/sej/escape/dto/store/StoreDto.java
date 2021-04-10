package com.sej.escape.dto.store;

import com.sej.escape.entity.geolocation.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreDto {

    private long id;
    private Address address;
    private List<String> area;
    private String name;
    private String link;
    private String tel;
    private long zim;
    private double good;
    private String imgUrl;
}
