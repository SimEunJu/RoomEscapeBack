package com.sej.escape.repository.board;

import com.sej.escape.entity.board.NoticeBoard;
import com.sej.escape.entity.board.ReqBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReqBoardRepository
        extends JpaRepository<ReqBoard, Long>, QuerydslPredicateExecutor<ReqBoard> {
}
