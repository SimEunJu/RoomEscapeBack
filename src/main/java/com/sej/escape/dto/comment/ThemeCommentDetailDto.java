package com.sej.escape.dto.comment;

import com.sej.escape.dto.file.FileResDto;
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

    private long id;
    private Ancestor theme;
    private Ancestor store;

    private int difficulty;
    private int flowerRoad;
    private boolean isEscape;
    private boolean isHidden;
    private String review;
    private double star;
    private int takenTime;
    private LocalDateTime visitDate;
    private int visitorNum;
    private List<String> etc;
    private int quizType;
    private int hints;

    private FileResDto[] uploadFiles;

    public void setVisitDate(LocalDate visitDate){
        this.visitDate = LocalDateTime.of(visitDate, LocalTime.MIDNIGHT);
    }
    public void setVisitDate(LocalDateTime visitDate){
        this.visitDate = visitDate;
    }

    public boolean isHorror(){
        if(etc == null) return false;
        return etc.contains("horror");
    }

    public boolean isActive(){
        if(etc == null) return false;
        return etc.contains("active");
    }
}
