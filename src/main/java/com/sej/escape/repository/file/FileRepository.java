package com.sej.escape.repository.file;

import com.querydsl.core.types.Predicate;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository<T extends File>
        extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

    @Modifying
    @Query("update File f set f.referId = :referId where f.id in :ids")
    int updateReferIds(@Param("referId") long referId, @Param("ids") List<Long> ids);

    @Modifying
    @Query("update #{#entityName} f set f.isDeleted = true, f.deleteDate = CURRENT_TIMESTAMP where f.id in :ids")
    int deleteFiles(@Param("ids") List<Long> ids);

    @Modifying
    @Query("update #{#entityName} f set f.isDeleted = true, f.deleteDate = CURRENT_TIMESTAMP where f.id = :id and f.member = :member")
    int deleteFile(@Param("id") long id, Member member);

    @Modifying
    @Query("update #{#entityName} f set f.isDeleted = true, f.deleteDate = CURRENT_TIMESTAMP where f.referId = :referId")
    int deleteFilesByReferId(@Param("referId") long referId);
}
