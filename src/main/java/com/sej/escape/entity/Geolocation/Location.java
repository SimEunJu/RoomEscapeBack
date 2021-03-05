package com.sej.escape.entity.Geolocation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Location {

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;
}
