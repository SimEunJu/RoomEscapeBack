package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Theme extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="theme_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="store_id")
    private Store store;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private List<ThemeComment> comments = new ArrayList<>();

    @Column(length = 2000)
    private String themeName;

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
    private Integer good;

    @Access(value=AccessType.PROPERTY)
    @Column(columnDefinition = "text")
    private String genre;

    public void setGenre(List<Genre> genres) {
        this.genre = mapEnumsToStr(genres);
    }

    public List<Genre> getGenre() {
        return mapStrToEnums(this.genre, Genre::valueOf);
    }

    @Access(value=AccessType.PROPERTY)
    private String quizType;

    public void setQuizType(List<QuizType> quizTypes) {
        this.quizType = mapEnumsToStr(quizTypes);
    }

    public List<QuizType> getQuizType() {
        return mapStrToEnums(this.quizType, QuizType::valueOf);
    }

    private <T extends Enum> String mapEnumsToStr(List<T> enums){
        return enums.stream()
                .map(T::toString)
                .collect(Collectors.joining(","));
    }

    private <T extends Enum> List<T> mapStrToEnums(String concatenated, Function<String, T> mapFunc){
        return Arrays.stream(concatenated.split(","))
                .map(mapFunc)
                .collect(Collectors.toList());
    }
}
