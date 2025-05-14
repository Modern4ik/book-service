package com.library.book_service.integration.semi.controller;

import com.library.book_service.controller.CommentController;
import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.service.comment.CommentService;
import com.library.book_service.utils.CommentTestUtils;
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

import java.time.LocalDateTime;

@WebMvcTest(CommentController.class)
@ActiveProfiles("test")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    public void shouldSaveNewComment() throws Exception {
        CommentRequestCreateDto createDto =
                CommentTestUtils.generateCommentCreateDto("Test content", 1, 1);
        CommentResponseDto expectedResponseDto =
                CommentTestUtils.generateResponseDto(
                        1L, createDto.content(), createDto.bookId(), createDto.userId(), LocalDateTime.now());

        Mockito.when(commentService.saveComment(createDto))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/monolith/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(createDto.content()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(expectedResponseDto.getBookId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(expectedResponseDto.getUserId()));

    }

    @Test
    public void shouldReturnCommentById() throws Exception {
        CommentResponseDto expectedDto = CommentTestUtils
                .generateResponseDto(1L, "Test content", 1, 1, LocalDateTime.now());

        Mockito.when(commentService.getCommentById(1L))
                .thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/monolith/comments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(expectedDto.getContent()));
    }

    @Test
    public void shouldReturnCommentsByBookIdAndUserId() throws Exception {
        CommentRequestFilterDto filterDto =
                CommentTestUtils.generateFilterDto(2, 2, LocalDateTime.now());

        Mockito.when(commentService.getComments(filterDto))
                .thenReturn(CommentTestUtils.generateCommentsDtoList(filterDto, 3));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/monolith/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookId").value(filterDto.bookId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(filterDto.userId()));
    }

    @Test
    public void shouldDeleteCommentById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/monolith/comments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
