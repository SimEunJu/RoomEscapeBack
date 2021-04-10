package com.sej.escape.entity.comment;

import com.sej.escape.entity.Board;
import com.sej.escape.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@AllArgsConstructor
@DiscriminatorValue("B")
public class BoardComment extends Comment {

    @Builder(builderMethodName = "boardCommentBuilder")
    public BoardComment(Long id, Member member, Long referId, String content,
                        Integer good, Integer report, Double star, int depth,
                        int seq, Long parId) {
        super(id, member, referId, content, good, report, star, depth, seq, parId);
    }
}
