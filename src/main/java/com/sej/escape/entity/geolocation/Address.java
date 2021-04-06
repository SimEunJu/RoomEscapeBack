package com.sej.escape.entity.geolocation;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    @Column(columnDefinition = "smallint unsigned")
    private Integer postcode;

    @Column(length = 1000)
    private String addr;

    @Column(length = 1000)
    private String detailAddr;

    @Column(length = 1000)
    private String addrEtc;
}
