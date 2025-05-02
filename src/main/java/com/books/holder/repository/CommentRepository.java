package com.books.holder.repository;

import com.books.holder.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Query("SELECT c FROM Comment c WHERE " +
            "(:bookId IS NULL OR c.book.id = :bookId) AND " +
            "(:userId IS NULL OR c.user.id = :userId) AND " +
            "(CAST(:createdAt AS TIMESTAMP) IS NULL OR c.createdAt = :createdAt)")
    List<Comment> findByFilters(Long bookId, Long userId, LocalDateTime createdAt);

}
