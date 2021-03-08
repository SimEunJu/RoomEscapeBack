package com.sej.escape.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDto {

    private long id;
    private String storeName;
    private String themeName;
    private String like;
}
