package com.sej.escape.dto.theme;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.annotation.security.DenyAll;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeForListDto {

    protected long id;
    protected String storeName;
    protected String name;
    protected double star;
    protected String imgUrl;
    @JsonProperty("isZimChecked")
    protected boolean isZimChecked;
    protected int zim;
    protected String introduce;

}
