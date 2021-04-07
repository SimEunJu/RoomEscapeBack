package com.sej.escape.repository.comment;

import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    @Query(nativeQuery = true)
    List<Comment> findAllByPaging(@Param("referId") long storeId, @Param("page") int page, @Param("size") int size);

}
