package com.library.book_service.integration.full;

import com.library.book_service.client.UserServiceClient;
import com.library.book_service.controller.CommentController;
import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.repository.CommentRepository;
import com.library.book_service.utils.CommentTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
@Sql(scripts = "/test-authors-data.sql")
@Sql(scripts = "/test-books-data.sql")
@Sql(scripts = "/test-comments-data.sql")
public class CommentIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentController commentController;
    @Autowired
    private CommentRepository commentRepository;

    @MockitoBean
    private UserServiceClient userServiceClient;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE books ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE comments ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveComment() {
        CommentRequestCreateDto createDto =
                CommentTestUtils.generateCommentCreateDto("Test content", 1L, 1L);

        Mockito.when(userServiceClient.existsById(1L))
                .thenReturn(true);

        CommentResponseDto responseDto = commentController.saveComment(createDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(4L, responseDto.getId());
        Assertions.assertEquals(createDto.content(), responseDto.getContent());
        Assertions.assertEquals(createDto.bookId(), responseDto.getBookId());
        Assertions.assertEquals(createDto.userId(), responseDto.getUserId());
        Assertions.assertNotNull(responseDto.getCreatedAt());

        Assertions.assertEquals(4, commentRepository.count());
    }

    @Test
    public void shouldReturnCommentById() {
        CommentResponseDto responseDto = commentController.getCommentById(1L);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals("First comment", responseDto.getContent());
    }

    @Test
    public void shouldReturnCommentsByBookIdAndUserId() {
        CommentRequestFilterDto filterDto =
                CommentTestUtils.generateFilterDto(1L, 1L, null);

        List<CommentResponseDto> responseDtoList = commentController.getComments(filterDto);

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(2, responseDtoList.size());
        for (CommentResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.bookId(), responseDto.getBookId());
            Assertions.assertEquals(filterDto.userId(), responseDto.getUserId());
        }
    }

    @Test
    public void shouldDeleteCommentById() {
        commentController.deleteCommentById(1L);

        Assertions.assertFalse(commentRepository.existsById(1L));
        Assertions.assertEquals(2, commentRepository.count());
    }

}
