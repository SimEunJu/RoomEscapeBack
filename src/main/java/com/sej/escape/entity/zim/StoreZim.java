package com.sej.escape.entity.zim;

import com.sej.escape.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("S")
public class StoreZim extends Zim{

    @Builder
    public StoreZim(Member member, boolean isZim, Long referId){
        super(member, isZim, referId);
    }
}
