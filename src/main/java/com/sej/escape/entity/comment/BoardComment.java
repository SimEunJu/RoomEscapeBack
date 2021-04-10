package com.sej.escape.entity.comment;

import com.sej.escape.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@SuperBuilder
@AllArgsConstructor
@DiscriminatorValue("B")
public class BoardComment extends Comment {

}
