package com.sej.escape.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    @ApiModelProperty("아이디") protected long id;
    @ApiModelProperty("내용") protected String content;

    @ApiModelProperty("작성자명") protected String writer;
    @ApiModelProperty("작성자 아이디") protected long writerId;

    @ApiModelProperty("부모 댓글 아이디") protected long parId;

    @ApiModelProperty("깊이") protected int depth;
    @ApiModelProperty("순서") protected int seq;

    @ApiModelProperty("좋아요 수") protected int good;
    @JsonProperty("isGoodChecked")
    @ApiModelProperty("로그인 사용자의 좋아요 여부") protected boolean isGoodChecked;

    @JsonProperty("isDeleted")
    @ApiModelProperty("삭제 여부") protected boolean isDeleted;

    @ApiModelProperty("별점") protected double star;

    @JsonProperty("isHidden")
    @ApiModelProperty("숨김 여부") protected boolean isHidden;

    @ApiModelProperty("등록날짜") protected LocalDateTime regDate;
    @ApiModelProperty("수정날짜") protected LocalDateTime updateDate;

    protected LocalDateTime deleteDate;

}
