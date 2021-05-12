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
@DiscriminatorValue("N")
public class NoticeBoard extends Board{

    @Builder(builderMethodName = "noticeBuilder")
    public NoticeBoard(Long id, Member member, List<NoticeBoardComment> comments, String title, String content, int good, int report, int view) {
        super(id, member, comments, title, content, good, report, view);
    }
}
