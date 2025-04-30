package com.books.holder.integration.full;

import com.books.holder.controller.BookController;
import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.repository.BookRepository;
import com.books.holder.utils.BookTestUtils;
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
@Sql(scripts = "/add-unknown-author.sql")
@Sql(scripts = "/test-books-data.sql")
@Sql(scripts = "/test-genres-data.sql")
public class BookIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookController bookController;
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE books ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE genres ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveBook() {
        BookRequestCreateDto createDto =
                BookTestUtils.generateBookCreateDto("Test book", 1, List.of(1, 2), 2000);

        BookResponseDto bookResponse = bookController.saveBook(createDto);

        Assertions.assertNotNull(bookResponse);
        Assertions.assertEquals(4L, bookResponse.id());
        Assertions.assertEquals(createDto.bookName(), bookResponse.bookName());
        Assertions.assertEquals(createDto.authorId(), bookResponse.authorId());
        Assertions.assertEquals(createDto.genresId().size(), bookResponse.genreNames().size());
        Assertions.assertTrue(bookResponse.genreNames().contains("Drama"));
        Assertions.assertTrue(bookResponse.genreNames().contains("Fantasy"));
        Assertions.assertEquals(createDto.publicationYear(), bookResponse.publicationYear());

        Assertions.assertEquals(4, bookRepository.count());
    }

    @Test
    public void shouldReturnBookById() {
        BookResponseDto bookResponse = bookController.getBookById(1L);

        Assertions.assertEquals(1, bookResponse.id());
        Assertions.assertEquals("BOOK ONE", bookResponse.bookName());
    }

    @Test
    public void shouldReturnBooksByNameAndAuthor() {
        BookRequestFilterDto filterDto =
                BookTestUtils.generateBookFilterDto("BOOK ONE", 1, null, null);

        List<BookResponseDto> booksResponse = bookController.getBooks(filterDto);

        Assertions.assertEquals(2, booksResponse.size());
        Assertions.assertEquals("BOOK ONE", booksResponse.get(0).bookName());
        Assertions.assertEquals(1, booksResponse.get(0).authorId());
    }

    @Test
    public void shouldUpdateBookById() {
        BookRequestUpdateDto updateDto =
                BookTestUtils.generateBookUpdateDto("UPDATED BOOK", null, List.of(1, 2), 2000);

        BookResponseDto bookResponse = bookController.updateBookById(1L, updateDto);

        Assertions.assertNotNull(bookResponse);
        Assertions.assertEquals("UPDATED BOOK", bookResponse.bookName());
        Assertions.assertEquals(updateDto.genresId().size(), bookResponse.genreNames().size());
        Assertions.assertTrue(bookResponse.genreNames().contains("Drama"));
        Assertions.assertTrue(bookResponse.genreNames().contains("Fantasy"));
        Assertions.assertEquals(2000, bookResponse.publicationYear());

    }

    @Test
    public void shouldDeleteBookById() {
        bookController.deleteBookById(1L);

        Assertions.assertFalse(bookRepository.existsById(1L));
        Assertions.assertEquals(2, bookRepository.count());
    }

}
