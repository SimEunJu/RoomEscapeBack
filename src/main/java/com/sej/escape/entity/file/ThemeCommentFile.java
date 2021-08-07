package com.sej.escape.entity.file;

import com.sej.escape.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("TC")
public class ThemeCommentFile extends File{

    @Builder(builderMethodName = "themeCommentBuilder")
    public ThemeCommentFile(Long id, Long referId, Member member, String originalName, String name, String rootPath, String subPath, Integer seq) {
        super(id, referId, member, originalName, name, rootPath, subPath, seq);
    }
}
