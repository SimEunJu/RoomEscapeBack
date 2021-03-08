package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;

public interface ThemeCommentRepository
        extends JpaRepository<ThemeComment, String>, QuerydslPredicateExecutor<ThemeComment> {

    @Query("select tc from ThemeComment tc where tc.isDeleted = false")
    Page<ThemeComment> findLatestComment(Pageable pageable);

    @Query("select tc from ThemeComment tc where tc.isDeleted = false and tc.regDate > :daysAgo")
    Page<ThemeComment> findTopComment(LocalDateTime daysAgo, Pageable pageable);
}
