package com.sej.escape.entity.board;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.comment.NoticeBoardComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("R")
public class ReqBoard extends Board{

    @Builder(builderMethodName = "reqBuilder")
    public ReqBoard(Long id, Member member, String btype, List<NoticeBoardComment> comments, String title, String content, int good, int report, int view) {
        super(id, member, btype, comments, title, content, good, report, view);
    }
}
