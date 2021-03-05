package com.sej.escape.entity.Geolocation;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private Integer postcode;
    private String addr;
    private String detailAddr;
    private String addrEtc;
}
