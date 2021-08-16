package com.sej.escape.entity;

import com.sej.escape.entity.base.BaseWithDelete;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import com.sej.escape.entity.file.ThemeFile;
import com.sej.escape.utils.RangeClosed;
import lombok.*;
import org.springframework.util.StringUtils;

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
    @Transient
    private List<ThemeFile> files = new ArrayList<>();

    @Column(length = 2000)
    private String name;

    @Column(length = 3000)
    private String link;

    @Column(columnDefinition = "smallint unsigned")
    private Integer minutes;

    @Transient
    private RangeClosed<Integer> personnel;

    @Access(value=AccessType.PROPERTY)
    @Column(length = 100)
    public String getPersonnel() {
        if(this.personnel == null) return null;
        return mapListToStr(personnel.getByList());
    };

    @Access(value=AccessType.PROPERTY)
    public void setPersonnel(String personnel){
        if(!StringUtils.hasText(personnel)) return;
        this.personnel = new RangeClosed<Integer>(mapStrToList(personnel, Integer::valueOf));
    }

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
    public String getGenre() { return mapListToStr(this.genre); };

    @Access(value=AccessType.PROPERTY)
    public void setGenre(String genre){
        this.genre = mapStrToList(genre, Genre::valueOf);
    }

    public List<Genre> getGenreByList() {
        return this.genre;
    }

    @Transient
    private List<QuizType> quizType;

    @Column(columnDefinition = "text")
    @Access(value=AccessType.PROPERTY)
    public String getQuizType() { return mapListToStr(this.quizType); };

    @Access(value=AccessType.PROPERTY)
    public void setQuizType(String quizType){
        this.quizType = mapStrToList(quizType, QuizType::valueOf);
    }

    public List<QuizType> getQuizTypeByList() {
        return this.quizType;
    }

    private <T> String mapListToStr(List<T> values){
        if(values == null) return null;
        return values.stream()
                .map(T::toString)
                .collect(Collectors.joining(","));
    }


    private <T> List<T> mapStrToList(String enumStr, Function<String, T> mapFunc){
        if(!StringUtils.hasText(enumStr)) return null;
        return Arrays.stream(enumStr.split(","))
                .map(mapFunc)
                .collect(Collectors.toList());
    }
}
