package com.sej.escape.entity.comment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class StoreComment extends Comment {
}
