package com.sej.escape.entity.comment;

import com.sej.escape.entity.Store;

import javax.persistence.*;

@Entity
@DiscriminatorValue("S")
public class StoreComment extends Comment {

    @Column(columnDefinition = "double default 0")
    private double star;

}
