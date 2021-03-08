package com.sej.escape.repository;

import com.sej.escape.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository
        extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {
}
