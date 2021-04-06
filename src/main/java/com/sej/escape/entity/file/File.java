package com.sej.escape.entity.file;

import com.sej.escape.entity.base.BaseWithDelete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ftype")
public class File extends BaseWithDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    protected Long id;

    protected Long referId;

    protected String originalName;

    protected String name;

    protected String rootPath;

    protected String subPath;

    protected Integer seq;

    public String getFileUrl(){
        return rootPath + "/" + subPath;
    }

}