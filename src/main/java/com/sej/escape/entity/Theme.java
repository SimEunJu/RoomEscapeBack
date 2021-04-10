package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
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
    @OneToMany(mappedBy = "theme")
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

    private String genre;

    public void setGenre(List<Genre> genres) {
        this.genre = mapEnumsToStr(genres);
    }

    @Access(value=AccessType.PROPERTY)
    @Column(columnDefinition = "text")
    public String getGenre() { return this.genre; };

    public List<Genre> getGenreByList() {
        return mapStrToEnums(this.genre, Genre::valueOf);
    }

    private String quizType;

    public void setQuizType(List<QuizType> quizTypes) {
        this.quizType = mapEnumsToStr(quizTypes);
    }

    @Column(columnDefinition = "text")
    @Access(value=AccessType.PROPERTY)
    public String getQuizType() { return this.quizType; };

    public List<QuizType> getQuizTypeByList() {
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
