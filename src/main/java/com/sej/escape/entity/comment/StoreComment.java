package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("S")
public class StoreComment extends Comment {

    @Builder(builderMethodName = "storeBuilder")
    public StoreComment(Long id, Member member, Long referId, String content,
                        int good, int report, double star, int depth, int seq, Long parId) {
        super(id, member, referId, content, good, report, star, depth, seq, parId);
    }
}
