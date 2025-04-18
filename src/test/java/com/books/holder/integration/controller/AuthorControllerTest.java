package com.books.holder.integration.controller;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.entity.Author;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.utils.AuthorTestUtils;
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

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class AuthorControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    private final AuthorRepository authorRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveAuthor() throws Exception {
        AuthorRequestCreateDto createDto =
                AuthorTestUtils.generateAuthorCreateDto("Sergey", null, null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        Author savedAuthor = authorRepository.findById(1).orElse(null);
        Assertions.assertNotNull(savedAuthor);
        Assertions.assertEquals(1, savedAuthor.getId());
        Assertions.assertEquals("Sergey", savedAuthor.getFirstName());
        Assertions.assertEquals(1, authorRepository.count());
    }

    @Test
    @Sql(scripts = "/test-authors-data.sql")
    public void shouldGetAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @Sql(scripts = "/test-authors-data.sql")
    public void shouldReturnAuthorsByFirstNameAndBirthday() throws Exception {
        AuthorRequestDto filterDto =
                AuthorTestUtils.generateAuthorFilterDto(
                        "Sergey", null, LocalDate.parse("1993-10-11"), null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Sergey"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1993-10-11"));

        Assertions.assertEquals(4, authorRepository.count());
    }

    @Test
    @Sql(scripts = "/test-authors-data.sql")
    public void shouldDeleteAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(authorRepository.existsById(2));
        Assertions.assertEquals(3, authorRepository.count());
    }

    @Test
    @Sql(scripts = "/test-authors-data.sql")
    public void shouldNotDeleteAuthorWithIdOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", 1))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors['deleteAuthorById.id']").value("Should not be equals one"));
    }

}
