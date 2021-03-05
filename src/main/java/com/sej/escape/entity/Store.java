package com.sej.escape.entity;

import com.sej.escape.entity.Geolocation.Address;
import com.sej.escape.entity.Geolocation.Location;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Embedded
    private Location location;
    @Embedded
    private Address address;

    private String name;
    private String link;
    private String phoneNumber;

    private Integer star;
    private Integer like;

}
