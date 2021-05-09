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

import java.util.List;
import java.util.Optional;

public interface StoreRepository
        extends JpaRepository<Store, Long>, QuerydslPredicateExecutor<Store> {

    Page<Store> findAllByIsDeletedFalse(BooleanBuilder builder, Pageable pageable);

    Optional<Store> findByIdAndIsDeletedFalse(long id);

    List<Store> findAllByIsDeletedFalseAndStoreNameContaining(String storeName);

    @Query("select s, sz from Store s inner join StoreZim sz on sz.referId = s.id and sz.isZim = true and sz.member = :member where s.isDeleted = false")
    Page<Object[]> findallByZim(@Param("member") Member memer, Pageable pageable);

    @Query("select t.store from Theme t where t.id = :themeId")
    Optional<Store> findByTheme(@Param("themeId") long id);
}
