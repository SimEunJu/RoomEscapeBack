package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(1)
    @Max(5)
    private Integer difficulty;

    @Column(columnDefinition = "text")
    private String introduce;

    @Transient
    private List<Genre> genre;

    @Access(value=AccessType.PROPERTY)
    @Column(columnDefinition = "text")
    public String getGenre() { return mapEnumsToStr(this.genre); };

    @Access(value=AccessType.PROPERTY)
    public void setGenre(String genre){
        this.genre = mapStrToEnums(genre, Genre::valueOf);
    }

    public List<Genre> getGenreByList() {
        return this.genre;
    }

    @Transient
    private List<QuizType> quizType;

    @Column(columnDefinition = "text")
    @Access(value=AccessType.PROPERTY)
    public String getQuizType() { return mapEnumsToStr(this.quizType); };

    @Access(value=AccessType.PROPERTY)
    public void setQuizType(String quizType){
        this.quizType = mapStrToEnums(quizType, QuizType::valueOf);
    }

    public List<QuizType> getQuizTypeByList() {
        return this.quizType;
    }

    private <T extends Enum> String mapEnumsToStr(List<T> enums){
        return enums.stream()
                .map(T::toString)
                .collect(Collectors.joining(","));
    }

    private <T> List<T> mapStrToEnums(String enumStr, Function<String, T> mapFunc){
        return Arrays.stream(enumStr.split(","))
                .map(mapFunc)
                .collect(Collectors.toList());
    }
}
