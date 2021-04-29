package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ctype")
public class Comment extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    protected Long referId;

    @Column(columnDefinition = "text")
    protected String content;

    @Column(columnDefinition = "int(2) default 0", nullable = false)
    protected int good;

    @Column(columnDefinition = "int(2) default 0", nullable = false)
    protected double star;

    // 대댓글 깊이
    @Column(columnDefinition = "int default 0")
    protected int depth;

    // 대댓글 순서
    @Column(columnDefinition = "int default 0")
    protected int seq;

    // 부모 댓글
    @Column
    protected Long parId;

    private boolean isHidden;

}
