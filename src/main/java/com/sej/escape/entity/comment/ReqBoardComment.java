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
@DiscriminatorValue("BR")
public class ReqBoardComment extends Comment {
    @Builder(builderMethodName = "reqBoardBuilder")
    public ReqBoardComment(Long id, Member member, Long referId, String content, int good, double star, int depth, int seq, Long parId, boolean isHidden) {
        super(id, member, referId, content, good, star, depth, seq, parId, isHidden);
    }
}
