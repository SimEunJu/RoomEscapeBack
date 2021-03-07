package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.constants.QuizType;
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
public class Theme extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="theme_id")

    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Store store;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    private List<ThemeComment> comments = new ArrayList<>();

    @Column(length = 2000)
    private String name;

    @Column(length = 3000)
    private String link;

    @Column(columnDefinition = "smallint unsigned")
    private Integer minutes;

    @Column(columnDefinition = "tinyint unsigned")
    private Integer personnel;

    @Column(columnDefinition = "tinyint unsigned")
    private Integer difficulty;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer star;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer like;

    // TODO: 장르 enum 정의해야
    private String genre;

    // TODO: 아래 정의된 getter setter 쓰도록
    private Integer quizType;

    public void setQuizType(List<QuizType> quizTypes) {
        this.quizType = QuizType.getThemeQTypesValSum(quizTypes);
    }

    public List<QuizType> getQuizType() {
        return QuizType.getThemeQTypes(this.quizType);
    }
}
