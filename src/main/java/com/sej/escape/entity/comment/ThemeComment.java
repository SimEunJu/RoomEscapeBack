package com.sej.escape.entity.comment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class ThemeComment extends Comment {
}
