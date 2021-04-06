package com.sej.escape.repository;

import com.sej.escape.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FileRepository
        extends JpaRepository<File, String>, QuerydslPredicateExecutor<File> {
}
