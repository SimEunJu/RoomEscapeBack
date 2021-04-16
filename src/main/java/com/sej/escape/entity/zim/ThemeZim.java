package com.sej.escape.entity.zim;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("T")
public class ThemeZim extends Zim{

    @Builder(builderMethodName = "themeBuilder")
    public ThemeZim(Long id, Member member, boolean isZim, Long referId) {
        super(id, member, isZim, referId);
    }
}
