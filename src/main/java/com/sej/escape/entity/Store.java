package com.sej.escape.entity;

import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.geolocation.Address;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column
    private Point location;

    @Embedded
    private Address address;

    @Column
    private double areaCode;

    @Column(length = 2000)
    private String storeName;

    @Column(length = 3000)
    private String link;

    @Column(length = 100)
    private String tel;

    @Column(columnDefinition = "text")
    private String introduce;

    @Column(columnDefinition = "smallint default 0")
    private int themeCnt;

}
