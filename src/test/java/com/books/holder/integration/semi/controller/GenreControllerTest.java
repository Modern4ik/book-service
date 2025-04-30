package com.books.holder.integration.semi.controller;

import com.books.holder.controller.GenreController;
import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.service.genre.GenreService;
import com.books.holder.utils.GenreTestUtils;
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

@WebMvcTest(GenreController.class)
@ActiveProfiles("test")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GenreService genreService;

    @Test
    public void shouldSaveGenre() throws Exception {
        GenreRequestCreateDto createDto = GenreTestUtils.generateGenreCreateDto("Drama");
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Drama");

        Mockito.when(genreService.saveGenre(createDto))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.name()));
    }

    @Test
    public void shouldReturnGenreById() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Drama");

        Mockito.when(genreService.getGenreById(expectedResponseDto.id()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/genres/by-id/{id}", expectedResponseDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.name()));
    }

    @Test
    public void shouldReturnGenreByName() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Drama");

        Mockito.when(genreService.getGenreByName(expectedResponseDto.name()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/genres/by-name/{name}", expectedResponseDto.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.name()));
    }

    @Test
    public void shouldReturnAllGenres() throws Exception {
        List<GenreResponseDto> expectedResponseList =
                GenreTestUtils.generateGenreResponseDtosListWithoutFilter(3);

        Mockito.when(genreService.getGenres())
                .thenReturn(expectedResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/genres"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Drama"));
    }

    @Test
    public void shouldUpdateGenreNameById() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Fantasy");

        Mockito.when(genreService.updateGenreNameById(expectedResponseDto.id(), expectedResponseDto.name()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/genres/{id}", expectedResponseDto.id())
                        .param("name", expectedResponseDto.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.name()));
    }

    @Test
    public void shouldDeleteGenreById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/genres/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
