package com.sej.escape.dto;

import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class CommentDto {

    private long id;
    private String content;
    private int like;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
