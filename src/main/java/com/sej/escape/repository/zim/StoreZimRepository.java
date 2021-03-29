package com.sej.escape.repository.zim;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface StoreZimRepository
        extends JpaRepository<StoreZim, Long>, QuerydslPredicateExecutor<Zim> {

    public StoreZim findByReferIdAndMember(long referId, Member member);
}
