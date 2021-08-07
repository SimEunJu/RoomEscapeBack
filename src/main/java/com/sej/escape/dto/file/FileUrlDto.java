package com.sej.escape.dto.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileUrlDto {
    private String subPath;
    private String name;
}
