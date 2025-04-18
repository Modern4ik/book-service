package com.books.holder.integration.controller;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.entity.Book;
import com.books.holder.repository.BookRepository;
import com.books.holder.utils.BookTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql(scripts = "/add-unknown-author.sql")
@Sql(scripts = "/test-books-data.sql")
public class BookControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    private final BookRepository bookRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE books ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    @Sql(scripts = "/add-unknown-author.sql")
    public void shouldSaveBook() throws Exception {
        BookRequestCreateDto createDto =
                BookTestUtils.generateBookCreateDto("New book", 1, 2000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        Book newBook = bookRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(newBook);
        Assertions.assertEquals(1L, newBook.getId());
        Assertions.assertEquals(1, newBook.getAuthor().getId());
        Assertions.assertEquals(2000, newBook.getPublicationYear());
        Assertions.assertEquals(1, bookRepository.count());
    }

    @Test
    public void shouldReturnBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("BOOK ONE"));

        Assertions.assertEquals(3, bookRepository.count());
    }

    @Test
    public void shouldReturnBooksByNameAndAuthor() throws Exception {
        BookRequestDto filterDto =
                BookTestUtils.generateBookFilterDto("BOOK ONE", 1, null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookName").value("BOOK ONE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorId").value(1));

        Assertions.assertEquals(3, bookRepository.count());
    }

    @Test
    public void shouldUpdateBookById() throws Exception {
        BookRequestUpdateDto updateDto =
                BookTestUtils.generateUpdateDto("UPDATED BOOK", null, 2000);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("UPDATED BOOK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2000));

        Book updatedBook = bookRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(updatedBook);
        Assertions.assertEquals("UPDATED BOOK", updatedBook.getBookName());
        Assertions.assertEquals(2000, updatedBook.getPublicationYear());

    }

    @Test
    public void shouldDeleteBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(bookRepository.existsById(1L));
        Assertions.assertEquals(2, bookRepository.count());
    }

}
