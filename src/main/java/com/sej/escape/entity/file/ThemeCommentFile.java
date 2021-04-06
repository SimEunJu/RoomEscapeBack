package com.sej.escape.entity.file;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TC")
public class ThemeCommentFile extends File{
}
