package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ThemeRepository
        extends JpaRepository<Theme, String>, QuerydslPredicateExecutor<Theme> {
}
