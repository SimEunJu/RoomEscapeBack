package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    Optional<Member> findMemberByEmail(String email);

}
