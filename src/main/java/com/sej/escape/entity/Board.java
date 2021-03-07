package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private List<BoardComment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private List<File> files = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer like;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer report;
}
