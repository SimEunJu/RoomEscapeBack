package com.sej.escape.entity.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentMap {
    // TODO: 이렇게 하는 게 맞나...
    // comment
    private Comment comment;
    private Integer id;

}
