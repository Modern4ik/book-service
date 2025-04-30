package com.books.holder.integration.full;

import com.books.holder.controller.CommentController;
import com.books.holder.dto.comment.CommentRequestCreateDto;
import com.books.holder.dto.comment.CommentRequestFilterDto;
import com.books.holder.dto.comment.CommentResponseDto;
import com.books.holder.repository.CommentRepository;
import com.books.holder.utils.CommentTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-authors-data.sql")
@Sql(scripts = "/test-books-data.sql")
@Sql(scripts = "/test-users-data.sql")
@Sql(scripts = "/test-comments-data.sql")
public class CommentIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentController commentController;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE books ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE users ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE comments ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveComment() {
        CommentRequestCreateDto createDto =
                CommentTestUtils.generateCommentCreateDto("Test content", 1L, 2L);

        CommentResponseDto responseDto = commentController.saveComment(createDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(4L, responseDto.id());
        Assertions.assertEquals(createDto.content(), responseDto.content());
        Assertions.assertEquals(createDto.bookId(), responseDto.bookId());
        Assertions.assertEquals(createDto.userId(), responseDto.userId());

        Assertions.assertEquals(4, commentRepository.count());
    }

    @Test
    public void shouldReturnCommentById() {
        CommentResponseDto responseDto = commentController.getCommentById(1L);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1L, responseDto.id());
        Assertions.assertEquals("First comment", responseDto.content());
    }

    @Test
    public void shouldReturnCommentsByBookIdAndUserId() {
        CommentRequestFilterDto filterDto =
                CommentTestUtils.generateFilterDto(1L, 1L, null);

        List<CommentResponseDto> responseDtoList = commentController.getComments(filterDto);

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(2, responseDtoList.size());
        for (CommentResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.bookId(), responseDto.bookId());
            Assertions.assertEquals(filterDto.userId(), responseDto.userId());
        }
    }

    @Test
    public void shouldDeleteCommentById() {
        commentController.deleteCommentById(1L);

        Assertions.assertFalse(commentRepository.existsById(1L));
        Assertions.assertEquals(2, commentRepository.count());
    }

}
