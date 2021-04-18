package com.sej.escape.repository.good;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.good.StoreGood;
import com.sej.escape.entity.zim.StoreZim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StoreGoodRepository
        extends JpaRepository<StoreGood, Long>, QuerydslPredicateExecutor<StoreGood> {

    public Optional<StoreGood> findByReferIdAndMember(long referId, Member member);
}
