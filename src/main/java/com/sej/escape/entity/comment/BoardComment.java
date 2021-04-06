package com.sej.escape.entity.comment;

import com.sej.escape.entity.Board;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("B")
public class BoardComment extends Comment {

}
