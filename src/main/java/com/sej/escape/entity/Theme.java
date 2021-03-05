package com.sej.escape.entity;

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
public class Theme extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private Store store;

    private String name;
    private String link;

    private Integer minutes;
    private Integer personnel;
    private Integer difficulty;
    private String genre;

    private Integer quizType;

    public void setQuizType(Integer quizType) {
        this.quizType = quizType;
    }
}
