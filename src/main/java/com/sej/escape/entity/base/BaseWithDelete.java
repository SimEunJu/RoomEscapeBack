package com.sej.escape.entity.base;

import com.sej.escape.entity.base.Base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseWithDelete extends Base {

    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean isDeleted;
    private LocalDateTime deleteDate;

}
