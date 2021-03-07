package com.sej.escape.entity.comment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class BoardComment extends Comment {
}
