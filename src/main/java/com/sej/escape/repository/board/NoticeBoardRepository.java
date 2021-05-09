package com.sej.escape.repository.board;

import com.sej.escape.entity.board.Board;
import com.sej.escape.entity.board.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NoticeBoardRepository
        extends JpaRepository<NoticeBoard, Long>, QuerydslPredicateExecutor<NoticeBoard> {
}
