package com.sej.escape.dto.good;

import com.sej.escape.constants.dto.GoodType;
import com.sej.escape.error.ErrorRes;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoodResDto {

    private Long id;
    private Long referId;
    private GoodType type;
    private Boolean isChecked;
    private Integer cnt;
    private boolean hasError;
    private ErrorRes error;

    public String getType() {
        return type.getTypeString();
    }
}
