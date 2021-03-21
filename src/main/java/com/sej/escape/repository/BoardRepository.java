package com.sej.escape.repository;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.dto.page.SearchReqDto;
import com.sej.escape.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface BoardRepository
        extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

    @Modifying
    @Query("update Board b set b.isDeleted = true, b.deleteDate = current_timestamp where b.id in :ids")
    long updateDeleteTAllByIdIn(List<Long> ids);

    Page<Board> findAllByDeletedNot(BooleanBuilder builder, Pageable pageable);

    Optional<Board> findByIdByDeletedNot(long id);
}
