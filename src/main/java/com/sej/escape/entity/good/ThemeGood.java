package com.sej.escape.entity.good;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.Zim;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("T")
public class ThemeGood extends Good {

    @Builder(builderMethodName = "themeBuilder")
    public ThemeGood(Long id, Member member, boolean isGood, Long referId, String gtype) {
        super(id, member, isGood, referId, gtype);
    }
}
