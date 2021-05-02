package com.sej.escape.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreZimListResDto {

    private long id;
    private long zimId;
    private String name;
    private String imgUrl;
    @JsonProperty("isZimChecked")
    private boolean isZimChecked;
}
