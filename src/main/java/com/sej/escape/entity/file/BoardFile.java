package com.sej.escape.entity.file;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class BoardFile extends File{
}
