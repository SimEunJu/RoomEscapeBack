package com.sej.escape.repository.comment;

import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.comment.StoreComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardCommentRepository
        extends JpaRepository<BoardComment, Long>, QuerydslPredicateExecutor<BoardComment> {
}
