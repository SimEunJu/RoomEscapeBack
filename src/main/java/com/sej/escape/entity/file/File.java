package com.sej.escape.entity.file;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.BaseWithDelete;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
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

    @Column(name = "ftype", insertable = false, updatable = false)
    private String ftype;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    protected String originalName;

    protected String name;

    protected String rootPath;

    protected String subPath;

    protected Integer seq;

    public String getFileUrl(){
        return rootPath + "/" + subPath + "/" + name;
    }

}