package com.sej.escape.repository.zim;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.Zim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ZimRepository<T extends Zim>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

    public Optional<T> findByReferIdAndMember(long referId, Member member);

}