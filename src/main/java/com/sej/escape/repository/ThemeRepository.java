package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;

public interface ThemeRepository
        extends JpaRepository<Theme, String>, QuerydslPredicateExecutor<Theme> {
    // 내가 하고 싶은 것 좋아요, 리뷰수, 최신순으로 정렬하고 싶어
    //@Query("select t, count( select tc from ThemeComment tc where tc.referId = t.id ) from Theme t where t.isDeleted = false and t.regDate > :monthAgo")
    //Page<Theme> findLatestThemes(LocalDateTime aMonthAgo, Pageable pageable);

    @Query("select t from Theme t where t.isDeleted = false")
    Page<Theme> findTopThemes(Pageable pageable);

}
