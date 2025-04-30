package com.books.holder.specifications;

import com.books.holder.dto.comment.CommentRequestFilterDto;
import com.books.holder.entity.Comment;
import com.books.holder.repository.BookRepository;
import com.books.holder.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentSpecification {

    public Specification<Comment> generateCommentSpec(CommentRequestFilterDto filterDto) {

        Specification<Comment> spec = Specification.where(null);

        Long bookId = filterDto.bookId();
        Long userId = filterDto.userId();
        LocalDateTime postDate = filterDto.postDate();

        if (bookId != null) {
            spec = hasBookId(bookId);
        }

        if (userId != null) {
            spec = hasUserId(userId);
        }

        if (postDate != null) {
            spec = hasPostDate(postDate);
        }

        return spec;
    }

    private Specification<Comment> hasBookId(Long bookId) {
        return (Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(root.get("book").get("id"), bookId);
    }

    private Specification<Comment> hasUserId(Long userId) {
        return (Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(root.get("user").get("id"), userId);
    }

    private Specification<Comment> hasPostDate(LocalDateTime postDate) {
        return (Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(root.get("postDate"), postDate);
    }

}
