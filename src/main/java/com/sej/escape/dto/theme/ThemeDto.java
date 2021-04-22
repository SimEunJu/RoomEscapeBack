package com.sej.escape.dto.theme;

import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto extends ThemeForListDto{

    private String link;
    private int minutes;
    private int personnel;
    private int difficulty;
    private List<Genre> genre;
    private List<QuizType> quizType;

    private List<ThemeForListDto> related;
}
