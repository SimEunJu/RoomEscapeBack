package com.sej.escape.entity.zim;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.base.Base;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ztype")
public class Zim extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zim_id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    @Column(nullable = false)
    protected boolean isZim;

    protected Long referId;

    public Zim(Member member, boolean isZim, Long referId){
        this.member = member;
        this.isZim = isZim;
        this.referId = referId;
    }
}
