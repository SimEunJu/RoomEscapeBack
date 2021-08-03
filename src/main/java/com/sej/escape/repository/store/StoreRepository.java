package com.sej.escape.repository.store;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface StoreRepository
        extends JpaRepository<Store, Long>, QuerydslPredicateExecutor<Store> {

    List<Store> findAllByNameContainsAndIsDeletedIsFalse(String name, Pageable pageable);

    // TODO: 연관관계 테이블을 따로 설정하지 않으니 매번 query에 명시해야 해서 불편
    @Query("select s, sz from Store s inner join StoreZim sz on sz.referId = s.id and sz.isZim = true and sz.member = :member and s.isDeleted = false")
    Page<Object[]> findAllByZim(@Param("member") Member member, Pageable pageable);

    @Query("select t.store from Theme t where t.id = :themeId and t.isDeleted = false")
    Optional<Store> findByTheme(@Param("themeId") long id);
}
