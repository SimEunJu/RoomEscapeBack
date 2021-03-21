package com.sej.escape.entity.base;

import com.sej.escape.entity.base.Base;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@Setter
public abstract class BaseWithDelete extends Base {

    @Column(columnDefinition = "tinyint(1) default 0")
    protected boolean isDeleted;
    protected LocalDateTime deleteDate;

}
