package com.sej.escape.dto.store;

import com.sej.escape.entity.geolocation.Address;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.awt.*;

@Getter
@Setter
public class StoreDto {

    private long id;
    private Point point;
    private Address address;
    private String storeName;
    private String link;
    private String phoneNumber;
    private long zim;
    private long good;
}
