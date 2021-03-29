package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="rtype")
public class Comment extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    // TODO: 더 나은 방법이 없을까
    // 보통 매핑 테이블 하나 더 두어서 관리 ex) 테이블1(comment-board), 테이블2(comment-theme) 등 
    protected Long referId;

    @Column(columnDefinition = "text")
    protected String content;

    @Column(columnDefinition = "int default 0", nullable = false)
    protected Integer good;

    @Column(columnDefinition = "int default 0", nullable = false)
    protected Integer report;

    // 대댓글 깊이
    @Column(columnDefinition = "int default 0", nullable = false)
    protected int depth;

    // 대댓글 순서
    @Column(columnDefinition = "int default 0", nullable = false)
    protected int seq;
}
