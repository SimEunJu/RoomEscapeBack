package com.sej.escape.repository;

import com.sej.escape.dto.page.SearchReqDto;
import com.sej.escape.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepository
        extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

    @Modifying
    @Query("update Board b set b.isDeleted = 1, b.deleteDate = current_timestamp where b.id in :ids")
    long updateDeleteTAllByIdIn(List<Long> ids);
}