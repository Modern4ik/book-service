package com.books.holder.integration.full;

import com.books.holder.controller.AuthorController;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.utils.AuthorTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/test-authors-data.sql")
public class AuthorIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuthorController authorController;
    @Autowired
    private AuthorRepository authorRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveAuthor() {
        AuthorRequestCreateDto createDto =
                AuthorTestUtils.generateAuthorCreateDto("Michail", null, null, null);

        AuthorResponseDto authorResponse = authorController.saveAuthor(createDto);

        Assertions.assertNotNull(authorResponse);
        Assertions.assertEquals(5, authorResponse.id());
        Assertions.assertEquals("Michail", authorResponse.firstName());
        Assertions.assertEquals(5, authorRepository.count());
    }

    @Test
    public void shouldGetAuthorById() {
        AuthorResponseDto authorResponse = authorController.getAuthorById(1);

        Assertions.assertNotNull(authorResponse);
        Assertions.assertEquals(1, authorResponse.id());
        Assertions.assertEquals("Unknown", authorResponse.firstName());
    }

    @Test
    public void shouldReturnAuthorsByFirstNameAndBirthday() {
        AuthorRequestFilterDto filterDto =
                AuthorTestUtils.generateAuthorFilterDto(
                        "Sergey", null, LocalDate.parse("1993-10-11"), null);

        List<AuthorResponseDto> authorsResponse = authorController.getAuthors(filterDto);

        Assertions.assertNotNull(authorsResponse);
        Assertions.assertEquals(2, authorsResponse.size());
        Assertions.assertEquals("Sergey", authorsResponse.get(0).firstName());
        Assertions.assertEquals("1993-10-11", authorsResponse.get(0).birthday().toString());
    }

    @Test
    public void shouldDeleteAuthorById() {
        authorController.deleteAuthorById(2);

        Assertions.assertFalse(authorRepository.existsById(2));
        Assertions.assertEquals(3, authorRepository.count());
    }

    @Test
    public void shouldNotDeleteAuthorWithIdOne() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> authorController.deleteAuthorById(1));
    }

}
