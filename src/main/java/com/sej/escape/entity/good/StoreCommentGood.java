package com.sej.escape.entity.good;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CS")
public class StoreCommentGood extends Good{
    @Builder(builderMethodName = "storeCommentBuilder")
    public StoreCommentGood(Long id, Member member, boolean isGood, Long referId) {
        super(id, member, isGood, referId);
    }
}
