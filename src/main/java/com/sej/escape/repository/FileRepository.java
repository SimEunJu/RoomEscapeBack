package com.sej.escape.repository;

import com.sej.escape.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository
        extends JpaRepository<File, String>, QuerydslPredicateExecutor<File> {

    @Modifying
    @Query("update File f set f.referId = :referId where f.id in :ids")
    int updateReferIds(@Param("referId") long referId, @Param("ids") List<Long> ids);

}
