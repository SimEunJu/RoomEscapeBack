package com.sej.escape.entity.board;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.NoticeBoardComment;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="btype")
public class Board extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder.Default
    @Transient
    private List<NoticeBoardComment> comments = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "int default 0")
    private int good;

    @Column(columnDefinition = "int default 0")
    private int report;

    @Column(columnDefinition = "int default 0")
    private int view;

}
