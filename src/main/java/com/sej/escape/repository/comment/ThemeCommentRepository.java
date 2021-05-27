package com.sej.escape.repository.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ThemeCommentRepository
        extends JpaRepository<ThemeComment, Long>, QuerydslPredicateExecutor<ThemeComment> {

    @Query("select tc from ThemeComment tc where tc.isDeleted = false")
    Page<ThemeComment> findLatestComments(Pageable pageable);

    @Query("select tc from ThemeComment tc join TopTrendingThemeComment ttc on tc.id = ttc.referId and tc.isDeleted = false and ttc.isActive = true")
    List<ThemeComment> findTopComments();

    @Query("select count(t.id), sum(case when (tc.id is not null) then 1 else 0 end) from Theme t left outer join ThemeComment tc on tc.isDeleted = false where t.store = :store")
    Object findThemeCntAndCommentCnt(@Param("store") Store store);

    @Query("select tc from ThemeComment tc where tc.isDeleted = false and tc.member = :member")
    Page<ThemeComment> findAllByMember(Pageable pageable, @Param("member") Member member);

    @Query("select tc, tcf from ThemeComment tc left outer join ThemeCommentFile tcf on tc.id = tcf.referId and tcf.isDeleted = false where tc.id = :commentId")
    Optional<Object> findDetailById(@Param("commentId") long commentId);
}
