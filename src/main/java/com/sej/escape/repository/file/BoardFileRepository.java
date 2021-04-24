package com.sej.escape.repository.file;

import com.sej.escape.entity.Member;
import com.sej.escape.entity.file.BoardFile;
import com.sej.escape.entity.good.StoreGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long>, QuerydslPredicateExecutor<BoardFile> {

}
