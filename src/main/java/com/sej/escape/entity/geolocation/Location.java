package com.sej.escape.entity.geolocation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Location {

    // 위도
    @Column(columnDefinition = "float(4,6)")
    private Double latitude;

    // 경도
    @Column(columnDefinition = "float(4,6)")
    private Double longitude;
}
