package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ThemeRepository
        extends JpaRepository<Theme, Long>, QuerydslPredicateExecutor<Theme> {

    @Query("select t from Theme t where t.isDeleted = false and t.regDate > :aMonthAgo")
    Page<Theme> findLatestThemes(@Param("aMonthAgo") LocalDateTime aMonthAgo, Pageable pageable);

    // TODO: 인기글은 나중에 통계처리
    // THINK: select에서 count 버리고 t만 가져올 수 있을까
    @Query("select t, t.comments.size as reviewCnt from Theme t where t.isDeleted = false group by t.id")
    Page<Theme> findTopThemes(Pageable pageable);

}
