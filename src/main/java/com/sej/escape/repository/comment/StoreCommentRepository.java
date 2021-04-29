package com.sej.escape.repository.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StoreCommentRepository
        extends JpaRepository<StoreComment, Long>, QuerydslPredicateExecutor<StoreComment> {

    @Query("select sc, s from StoreComment sc inner join Store s on sc.referId = s.id where s.isDeleted = false and sc.member = :member")
    List<Object[]> findAllByMember(Pageable pageable, @RequestParam("member") Member member);
}
