package com.sej.escape.entity.comment;

import com.sej.escape.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("S")
public class StoreComment extends Comment {

    @Column(columnDefinition = "double default 0")
    private double star;

}
