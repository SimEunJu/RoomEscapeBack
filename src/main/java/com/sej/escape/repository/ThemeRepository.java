package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.naming.event.ObjectChangeListener;
import java.time.LocalDateTime;
import java.util.List;

public interface ThemeRepository
        extends JpaRepository<Theme, Long>, QuerydslPredicateExecutor<Theme> {

    @Query("select t from Theme t where t.isDeleted = false and t.regDate > :aMonthAgo")
    Page<Theme> findLatestThemes(@Param("aMonthAgo") LocalDateTime aMonthAgo, Pageable pageable);

    // TODO: 인기글은 나중에 통계처리
    // THINK: select에서 count 버리고 t만 가져올 수 있을까
    @Query("select t, t.comments.size as reviewCnt from Theme t where t.isDeleted = false group by t.id")
    Page<Theme> findTopThemes(Pageable pageable);
    List<Theme> findAllByIsDeletedFalseAndStoreEquals(Store store);

    @Query("select t, tz, s from Theme t inner join ThemeZim tz on tz.referId = t.id and tz.isZim = true and tz.member = :member inner join Store s on t.store = s where t.isDeleted = false")
    Page<Object[]> findallByZim(@Param("member") Member memer, Pageable pageable);

    List<Theme> findAllByIsDeletedFalseAndThemeNameContaining(String themeName);
}
