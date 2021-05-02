package com.sej.escape.dto.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sej.escape.dto.store.StoreDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeZimListResDto {

    private long id;
    private long zimId;
    private String name;
    private String imgUrl;
    @JsonProperty("isZimChecked")
    private boolean isZimChecked;
    private String storeName;
}
