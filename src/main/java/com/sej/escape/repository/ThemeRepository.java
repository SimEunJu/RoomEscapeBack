package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.naming.event.ObjectChangeListener;
import java.time.LocalDateTime;
import java.util.List;

public interface ThemeRepository
        extends JpaRepository<Theme, Long>, QuerydslPredicateExecutor<Theme> {

    @EntityGraph(attributePaths = {"store"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select t from Theme t where t.isDeleted = false and t.regDate > :monthsAgo")
    Page<Theme> findLatestThemes(Pageable pageable, @Param("monthsAgo") LocalDateTime aMonthAgo);

    @EntityGraph(attributePaths = {"store"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select t from Theme t join TopTrendingTheme tt on t.id = tt.referId and tt.isActive = true")
    List<Theme> findTopThemes();

    List<Theme> findAllByIsDeletedFalseAndStoreEquals(Store store);

    @Query("select t, f from Theme t join ThemeFile f on f.ftype = 'T' and t.id = f.referId and f.isDeleted = false where t.store = :store and t.id <> :selfId and t.isDeleted = false")
    List<Object[]> findAllByIsDeletedFalseAndStoreEqualsAndIdIsNot(Store store, long selfId);

    // TODO: ThemeFile 조인 시 ftype 조건이 생성되지 않음 -> 임시방편: DiscriminatorColumn 직접 사용
    @Query("select t, tz, s, f from Theme t inner join ThemeZim tz on tz.ztype = 'T' and tz.referId = t.id and tz.isZim = true and tz.member = :member inner join Store s on t.store = s left join ThemeFile f on f.ftype = 'T' and f.referId = t.id and f.isDeleted = false where t.isDeleted = false")
    Page<Object[]> findAllByZim(@Param("member") Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"store"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Theme> findAllByAndNameContainsAndIsDeletedFalse(String name, Pageable pageable);
}
