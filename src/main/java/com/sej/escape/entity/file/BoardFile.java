package com.sej.escape.entity.file;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("B")
public class BoardFile extends File{

    @Builder(builderMethodName = "boardBuilder")
    public BoardFile(Long id, Long referId, String originalName,
                     String name, String rootPath, String subPath, Integer seq) {
        super(id, referId, originalName, name, rootPath, subPath, seq);
    }
}
