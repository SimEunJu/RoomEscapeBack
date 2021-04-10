package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
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

    @Column(columnDefinition = "int default 0", nullable = false)
    protected Integer good;

    @Column(columnDefinition = "int default 0", nullable = false)
    protected Integer report;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    protected Double star;

    // 대댓글 깊이
    @Column(columnDefinition = "int default 0", nullable = false)
    protected int depth;

    // 대댓글 순서
    @Column(columnDefinition = "int default 0", nullable = false)
    protected int seq;

    // 부모 댓글
    @Column(columnDefinition = "int default 0", nullable = false)
    protected Long parId;
}
