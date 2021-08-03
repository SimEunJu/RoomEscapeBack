package com.sej.escape.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ancestor {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("") private String name;

    @ApiModelProperty("종류") private String type;
    @ApiModelProperty("하위 종류") private List<String> subTypes;
}
