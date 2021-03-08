package com.sej.escape.entity.comment;

import com.sej.escape.entity.Theme;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("T")
public class ThemeComment extends Comment {

    @Transient
    private Theme theme;
}
