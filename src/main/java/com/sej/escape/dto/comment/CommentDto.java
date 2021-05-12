package com.sej.escape.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    protected long id;
    protected String content;
    protected String writer;

    protected long parId;
    protected int depth;
    protected int seq;
    protected int good;
    @JsonProperty("isGoodChecked")
    protected boolean isGoodChecked;
    @JsonProperty("isDeleted")
    protected boolean isDeleted;
    protected double star;
    @JsonProperty("isHidden")
    protected boolean isHidden;

    // TODO: LocalDateTime json mapping 일괄 처리 되도록 설정 알아볼 것것
    //@JsonDeserialize(using = LocalDateDeserializer.class)
    //@JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDateTime regDate;
    protected LocalDateTime updateDate;
    protected LocalDateTime deleteDate;

}
