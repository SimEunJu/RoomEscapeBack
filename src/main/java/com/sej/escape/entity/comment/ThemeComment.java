package com.sej.escape.entity.comment;

import com.sej.escape.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("T")
public class ThemeComment extends Comment {

    @Transient
    private Theme theme;
}
