package com.library.book_service.repository;

import com.library.book_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE " +
            "(:bookId IS NULL OR c.book.id = :bookId) AND " +
            "(:userId IS NULL OR c.userId = :userId) AND " +
            "(CAST(:createdAt AS TIMESTAMP) IS NULL OR c.createdAt = :createdAt)")
    List<Comment> findByFilters(Long bookId, Long userId, LocalDateTime createdAt);

    @Query("DELETE FROM Comment c WHERE c.userId = :userId")
    @Modifying
    void deleteByUserId(Long userId);

}
