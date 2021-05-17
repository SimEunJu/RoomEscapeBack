package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    Optional<Member> findMemberByEmail(String email);

    @Modifying
    @Query("update Member m set m.withdrawalDate = current_timestamp, m.isWithdrawal = true where m = :member")
    int withdrawal(@Param("member") Member member);

}
