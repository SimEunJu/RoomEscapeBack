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
    private Long Id;

    @ManyToOne
    @Column(nullable = false)
    private Member member;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer like;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer report;

    // 대댓글 깊이
    @Column(columnDefinition = "int default 0", nullable = false)
    private int depth;

    // 대댓글 순서
    @Column(columnDefinition = "int default 0", nullable = false)
    private int seq;
}
