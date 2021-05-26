package com.sej.escape.entity.trending;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("TC")
public class TopTrendingThemeComment extends TopTrending{
    @Builder(builderMethodName = "themeCommentBuilder")
    public TopTrendingThemeComment(Long id, Long referId, LocalDateTime regDate, boolean isActive) {
        super(id, referId, regDate, isActive);
    }
}
