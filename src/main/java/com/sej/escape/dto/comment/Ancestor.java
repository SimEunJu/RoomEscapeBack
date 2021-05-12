package com.sej.escape.dto.comment;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ancestor {

    private String type;
    private List<String> subTypes;
    private long id;

}
