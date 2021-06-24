package com.sej.escape.dto.zim;

import com.sej.escape.constants.dto.ZimType;
import com.sej.escape.error.ErrorRes;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZimResDto {

    private Long id;
    private Long referId;
    private ZimType type;
    private Boolean isChecked;
    private Integer cnt;
    private boolean hasError;
    private ErrorRes error;

    public String getType() {
        return type.getTypeString();
    }
}
