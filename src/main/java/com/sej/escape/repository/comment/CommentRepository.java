package com.sej.escape.repository.comment;

import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    @Modifying
    @Query("update Comment c set c.seq = c.seq+1 where c.parId = :parId and c.seq > :parSeq")
    int updateBelowCommentSeq(@Param("parId") long parId, @Param("parSeq") int parSeq);

}
