package com.sej.escape.dto.file;

import com.sej.escape.constants.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResDto {

    private String url;
    private int seq;
    private FileType type;
    private long id;
    private long randomId;
    
    // TODO: mapping configure 설정
    public String getType(){
        return this.type.toString().toLowerCase();
    }
}
