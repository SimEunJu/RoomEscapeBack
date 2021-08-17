package com.sej.escape.entity.file;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("S")
public class StoreFile extends File{

    @Builder(builderMethodName = "storeBuilder")
    public StoreFile(Long id, Long referId, String ftype, Member member, String originalName, String name, String rootPath, String subPath, Integer seq) {
        super(id, referId, ftype, member, originalName, name, rootPath, subPath, seq);
    }
}
