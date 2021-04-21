package com.sej.escape.entity.good;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.Zim;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("S")
public class StoreGood extends Good {

    @Builder(builderMethodName = "storeBuilder")

    public StoreGood(Long id, Member member, boolean isGood, Long referId) {
        super(id, member, isGood, referId);
    }
}