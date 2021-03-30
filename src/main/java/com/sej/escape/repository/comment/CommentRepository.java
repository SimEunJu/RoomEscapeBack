package com.sej.escape.repository.comment;

import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository
        extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
}
