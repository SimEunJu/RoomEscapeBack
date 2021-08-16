package com.sej.escape.dto.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.entity.geolocation.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreAreaDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("가게명") private String name;
    @JsonIgnore private double areaCode;
    @ApiModelProperty("주소") private Address address;


    public StoreAreaDto(long id, String name, double areaCode, String addr, String detailAddr) {
        this.id = id;
        this.name = name;
        this.areaCode = areaCode;
        this.address = new Address();
        this.address.setAddr(addr);
        this.address.setDetailAddr(detailAddr);
    }
}
