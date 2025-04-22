package com.books.holder.integration.semi.controller;

import com.books.holder.controller.AuthorController;
import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.service.AuthorService;
import com.books.holder.utils.AuthorTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@WebMvcTest(controllers = AuthorController.class)
@ActiveProfiles("test")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @Test
    public void shouldSaveAuthor() throws Exception {
        AuthorRequestCreateDto createDto =
                AuthorTestUtils.generateAuthorCreateDto("Michail", null, null, null);

        Mockito.when(authorService.saveAuthor(createDto))
                .thenReturn(AuthorTestUtils.generateAuthorResponseDto(
                        2, "Michail", null, null, null));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Michail"));
    }

    @Test
    public void shouldReturnAuthorById() throws Exception {
        Mockito.when(authorService.getAuthorById(1))
                .thenReturn(AuthorTestUtils.generateAuthorResponseDto(
                        1, "Unknown", null, null, null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Unknown"));
    }

    @Test
    public void shouldReturnAuthorsByFirstNameAndBirthday() throws Exception {
        AuthorRequestFilterDto filterDto =
                AuthorTestUtils.generateAuthorFilterDto(
                        "Sergey", null, LocalDate.parse("1993-10-11"), null);

        Mockito.when(authorService.getAuthors(filterDto))
                .thenReturn(AuthorTestUtils.generateAuthorResponseDtoList(filterDto, 2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Sergey"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthday").value("1993-10-11"));
    }

    @Test
    public void shouldDeleteAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldNotDeleteAuthorWithIdOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.errors['deleteAuthorById.id']").value("Should not be equals one"));
    }
}
