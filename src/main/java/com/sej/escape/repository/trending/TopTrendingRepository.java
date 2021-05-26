package com.sej.escape.repository.trending;

import com.sej.escape.entity.trending.TopTrending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TopTrendingRepository<T extends TopTrending>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

    @Modifying
    @Query("update TopTrendingTheme t set t.isActive = false where t.isActive = true")
    int updatePrevThemeTrendingInactive();

    @Modifying
    @Query("update TopTrendingThemeComment t set t.isActive = false where t.isActive = true")
    int updatePrevThemeCommentTrendingInactive();

}
