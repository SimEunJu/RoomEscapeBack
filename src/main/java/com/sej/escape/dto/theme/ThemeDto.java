package com.sej.escape.dto.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.entity.constants.Genre;
import com.sej.escape.entity.constants.QuizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto extends ThemeForListDto{

    @ApiModelProperty("테마 페이지 url") private String link;
    @ApiModelProperty("소요 시간") private int minutes;
    @ApiModelProperty("인원 수") private int personnel;
    @ApiModelProperty("난이도") private int difficulty;
    @ApiModelProperty("장르") private List<Genre> genre;
    @ApiModelProperty("문제 유형") private List<QuizType> quizType;

    @ApiModelProperty("같은 가게의 다른 테마들") private List<ThemeForListDto> related;
}
