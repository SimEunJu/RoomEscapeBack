package com.sej.escape.repository;

import com.sej.escape.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FileRepository
        extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {
}
