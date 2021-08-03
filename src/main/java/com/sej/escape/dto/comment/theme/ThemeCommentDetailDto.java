package com.sej.escape.dto.comment.theme;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sej.escape.dto.comment.Ancestor;
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
public class ThemeCommentDetailDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("테마") private Ancestor theme;
    @ApiModelProperty("가게") private Ancestor store;

    @ApiModelProperty("작성자명") private String writer;

    @ApiModelProperty("등록날짜") private LocalDateTime regDate;
    @ApiModelProperty("난이도") private int difficulty;
    @ApiModelProperty("꽃길") private int flowerRoad;
    @ApiModelProperty("탈출 여부") private boolean isEscape;
    @ApiModelProperty("숨김 여부") private boolean isHidden;
    @ApiModelProperty("후기") private String review;
    @ApiModelProperty("별점") private double star;
    @ApiModelProperty("소요 시간") private int takenTime;
    @ApiModelProperty("방문날짜") private LocalDateTime visitDate;
    @ApiModelProperty("방문인 수") private int visitorNum;

    @ApiModelProperty("기타") private List<String> etc;
    @ApiModelProperty("활동성 여부") private boolean isActive;
    @ApiModelProperty("공포 여부") private boolean isHorror;

    @ApiModelProperty("문제 종류") private int quizType;
    @ApiModelProperty("사용 힌트 수") private int hints;

    @ApiModelProperty("업로드 파일") private FileResDto[] uploadFiles;

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
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
