package com.sej.escape.dto.comment.theme;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.file.FileResDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ThemeCommentDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("후기 생성 시 사용되는 랜덤 아이디") private long randId;
    @ApiModelProperty("테마 아이디") private long themeId;

    @ApiModelProperty("난이도") private int difficulty;
    @ApiModelProperty("꽃길") private int flowerRoad;

    @JsonProperty("isEscape")
    @ApiModelProperty("탈출 여부") private boolean isEscape;

    @JsonProperty("isHidden")
    @ApiModelProperty("숨김 여부") private boolean isHidden;

    @ApiModelProperty("후기") private String review;
    @ApiModelProperty("별점") private double star;
    @ApiModelProperty("소요 시간") private int takenTime;
    @ApiModelProperty("방문날짜") private LocalDateTime visitDate;
    @ApiModelProperty("방문인 수") private int visitorNum;

    @ApiModelProperty("기타") private List<String> etc;

    @JsonProperty("isHorror")
    @ApiModelProperty("공포 여부") private boolean isHorror;

    @JsonProperty("isActive")
    @ApiModelProperty("활동성 여부") private boolean isActive;

    @ApiModelProperty("문제 종류") private int quizType;
    @ApiModelProperty("사용 힌트 수") private int hints;

    @ApiModelProperty("업로드 이미지 url") private String imgUrl;

    private FileResDto[] uploadFiles;

    public void setVisitDate(LocalDate visitDate){
        this.visitDate = LocalDateTime.of(visitDate, LocalTime.MIDNIGHT);
    }

    // req -> entity 관계에서만 사용
    @JsonIgnore
    public boolean isHorrorSet(){
        if(etc == null) return false;
        return etc.contains("horror");
    }

    // req -> entity 관계에서만 사용
    @JsonIgnore
    public boolean isActiveSet(){
        if(etc == null) return false;
        return etc.contains("active");
    }
}
