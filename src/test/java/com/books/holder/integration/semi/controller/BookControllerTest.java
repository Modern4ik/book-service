package com.books.holder.integration.semi.controller;

import com.books.holder.controller.BookController;
import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.service.book.BookService;
import com.books.holder.utils.BookTestUtils;
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

import java.util.List;

@WebMvcTest(controllers = BookController.class)
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    public void shouldSaveBook() throws Exception {
        BookRequestCreateDto createDto =
                new BookRequestCreateDto("Test Book", 1, List.of(1, 2), null);

        Mockito.when(bookService.saveBook(createDto))
                .thenReturn(BookTestUtils.generateBookResponseDto(
                        1L, createDto.bookName(), createDto.authorId(), List.of("Drama", "Fantasy"), createDto.publicationYear()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("Test Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreNames", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreNames[0]").value("Drama"));
    }

    @Test
    public void shouldReturnBookById() throws Exception {
        Mockito.when(bookService.getBookById(1L))
                .thenReturn(BookTestUtils.generateBookResponseDto(
                        1L, "Test Book", 1, List.of("Drama", "Fantasy"), null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("Test Book"));
    }

    @Test
    public void shouldReturnBooksByNameAndAuthor() throws Exception {
        BookRequestFilterDto filterDto =
                BookTestUtils.generateBookFilterDto("Filter book", 2, null, null);

        Mockito.when(bookService.getBooks(filterDto))
                .thenReturn(BookTestUtils.generateBookResponseDtoList(filterDto, 3));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookName").value("Filter book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorId").value(2));
    }

    @Test
    public void shouldUpdateBookById() throws Exception {
        BookRequestUpdateDto updateDto =
                BookTestUtils.generateBookUpdateDto("Updated book", 1, List.of(1, 2), 2000);

        Mockito.when(bookService.updateBookById(1L, updateDto))
                .thenReturn(BookTestUtils.generateBookResponseDto(
                        1L, updateDto.bookName(), updateDto.authorId(), List.of("Drama", "Fantasy"), updateDto.publicationYear()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("Updated book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreNames", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreNames[0]").value("Drama"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2000));
    }

    @Test
    public void shouldDeleteBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
