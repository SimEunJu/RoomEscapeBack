package com.sej.escape.dto.zim;

import com.sej.escape.error.ErrorRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ZimResDto {

    private Long id;
    private Long referId;
    private String type;
    private boolean isChecked;
    private int cnt;
    private boolean hasError;
    private ErrorRes error;
}
