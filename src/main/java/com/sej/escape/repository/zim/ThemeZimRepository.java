package com.sej.escape.repository.zim;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.ThemeZim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ThemeZimRepository
        extends JpaRepository<ThemeZim, Long>, QuerydslPredicateExecutor<ThemeZim> {

    Optional<ThemeZim> findByReferIdAndMember(long referId, Member member);
}
