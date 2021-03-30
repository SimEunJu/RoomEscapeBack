package com.sej.escape.dto;

import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private long id;
    private String content;
    private int depth;
    private int seq;
    private int good;
    private int star;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;
}
