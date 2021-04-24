package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.file.File;
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
public class Board extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder.Default
    @Transient
    private List<BoardComment> comments = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "int default 0")
    private Integer good;

    @Column(columnDefinition = "int default 0")
    private Integer report;

    @Column(columnDefinition = "int default 0")
    private Integer view;

}
