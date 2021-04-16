package com.sej.escape.entity.comment;

import com.sej.escape.entity.Board;
import com.sej.escape.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("B")
public class BoardComment extends Comment {

    @Builder(builderMethodName = "boardBuilder")
    public BoardComment(Long id, Member member, Long referId, String content,
                        int good, int report, double star, int depth, int seq, Long parId) {
        super(id, member, referId, content, good, report, star, depth, seq, parId);
    }
}
