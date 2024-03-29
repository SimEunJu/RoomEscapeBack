package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("BN")
public class NoticeBoardComment extends Comment{

    @Builder(builderMethodName = "noticeBoardBuilder")
    public NoticeBoardComment(Long id, Member member, String ctype, Long referId, String content, int good, double star, int depth, int seq, Long parId, boolean isHidden) {
        super(id, member, ctype, referId, content, good, star, depth, seq, parId, isHidden);
    }
}
