package com.sej.escape.repository.good;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.good.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface GoodRepository<T extends Good>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

    public Optional<T> findByReferIdAndMember(long referId, Member member);
}
