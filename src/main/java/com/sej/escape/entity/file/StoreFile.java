package com.sej.escape.entity.file;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class StoreFile extends File{
}
