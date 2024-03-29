package com.sej.escape.repository.board;

import com.sej.escape.entity.board.Board;
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
    int updateDeleteAllByIdIn(List<Long> ids);

    @Modifying
    @Query("update Board b set b.view = b.view+1 where b = :board")
    int updateViewCnt(Board board);

    Optional<Board> findByIdAndIsDeletedFalse(long id);
}
