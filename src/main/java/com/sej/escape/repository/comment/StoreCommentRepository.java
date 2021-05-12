package com.sej.escape.repository.comment;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StoreCommentRepository
        extends JpaRepository<StoreComment, Long>, QuerydslPredicateExecutor<StoreComment> {

    @Query("select sc, s from StoreComment sc inner join Store s on sc.referId = s.id where sc.isDeleted = false and sc.member = :member")
    Page<Object[]> findAllByMember(Pageable pageable, @RequestParam("member") Member member);

    @Query("select sc, s from StoreComment sc inner join Store s on sc.referId = s.id where sc.isDeleted = false and sc.member = :member and sc.id = :commentId")
    Object findByIdAndMember(@RequestParam("member") Member member, @RequestParam("commentId") long commentId);

    /*
    @Modifying
    @Query("update from StoreComment sc set parId = :pardId where id = :id")
    int updateParIdById(@RequestParam("parId") long parId, @RequestParam("id") long id);
    */
}

