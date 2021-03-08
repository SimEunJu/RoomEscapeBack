package com.sej.escape.entity.comment;

import com.sej.escape.entity.Store;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("S")
public class StoreComment extends Comment {

    @Transient
    private Store store;

}
