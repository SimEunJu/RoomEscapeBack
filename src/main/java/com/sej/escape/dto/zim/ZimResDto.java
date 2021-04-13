package com.sej.escape.dto.zim;

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
    private String type;
    private Boolean isChecked;
    private int cnt;
    private boolean hasError;
    private ErrorRes error;
}