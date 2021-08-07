package com.sej.escape.repository.comment;

import com.sej.escape.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardCommentRepository<T extends Comment>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {
}
