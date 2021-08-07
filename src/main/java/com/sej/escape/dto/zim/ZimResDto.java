package com.sej.escape.dto.zim;

import com.sej.escape.constants.dto.ZimType;
import com.sej.escape.error.ErrorRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZimResDto {

    @ApiModelProperty("아이디") private Long id;
    @ApiModelProperty("찜 사용하는 도메인 아이디") private Long referId;
    private ZimType type;

    @ApiModelProperty("활성화 여부") private Boolean isChecked;

    //private Integer cnt;
    private boolean hasError;
    private ErrorRes error;

    @ApiModelProperty("종류")
    public String getType() {
        return type.getTypeString();
    }
}
