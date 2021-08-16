package com.sej.escape.entity.geolocation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Address {

    @Column(length=100)
    private String postcode;

    @Column(length = 1000)
    private String addr;

    @Column(length = 1000)
    private String detailAddr;

    @Column(length = 1000)
    private String addrEtc;
}
