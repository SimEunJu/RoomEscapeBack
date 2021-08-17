package com.sej.escape.entity.good;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CT")
public class ThemeCommentGood extends Good{

    @Builder(builderMethodName = "themeCommentBuilder")
    public ThemeCommentGood(Long id, Member member, boolean isGood, Long referId, String gtype) {
        super(id, member, isGood, referId, gtype);
    }
}
