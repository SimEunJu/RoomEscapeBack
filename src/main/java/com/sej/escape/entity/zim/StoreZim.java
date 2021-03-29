package com.sej.escape.entity.zim;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class StoreZim extends Zim{
}
