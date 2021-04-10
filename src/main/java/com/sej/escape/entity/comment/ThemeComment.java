package com.sej.escape.entity.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.*;

import javax.annotation.processing.Generated;
import javax.jdo.annotations.Join;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeComment extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="theme_id", nullable = false)
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "int(2) default 0", nullable = false)
    private int good;

    @Column(nullable = false)
    private LocalDateTime visitDate;

    @Column(columnDefinition = "int(2) default 0", nullable = false)
    private int visitorNum;

    @Column(nullable = false)
    private boolean isEscape;

    @Column(columnDefinition = "int(2) default 0", nullable = false)
    private int diffculty;

    @Column(nullable = false)
    private boolean isSecret;

    @Column(columnDefinition = "text")
    private String review;

    @Column(columnDefinition = "int(2) default 0")
    private Integer flowerRoad;

    @Column(columnDefinition = "int(2) default 0")
    private Integer quizType;

    @Column(columnDefinition = "int(2) default 0")
    private Integer hints;

    private Integer takenTime;

    private boolean isHorror;

    private boolean isActive;

}
