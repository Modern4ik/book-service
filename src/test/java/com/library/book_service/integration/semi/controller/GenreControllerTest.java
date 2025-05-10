package com.library.book_service.integration.semi.controller;

import com.library.book_service.controller.GenreController;
import com.library.book_service.dto.genre.GenreRequestCreateDto;
import com.library.book_service.dto.genre.GenreResponseDto;
import com.library.book_service.service.genre.GenreService;
import com.library.book_service.utils.GenreTestUtils;
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/monolith/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.getName()));
    }

    @Test
    public void shouldReturnGenreById() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Drama");

        Mockito.when(genreService.getGenreById(expectedResponseDto.getId()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/monolith/genres/{id}", expectedResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.getName()));
    }

    @Test
    public void shouldReturnGenreByName() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Drama");

        Mockito.when(genreService.getGenreByName(expectedResponseDto.getName()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/monolith/genres/by-name/{name}", expectedResponseDto.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.getName()));
    }

    @Test
    public void shouldReturnAllGenres() throws Exception {
        List<GenreResponseDto> expectedResponseList =
                GenreTestUtils.generateGenreResponseDtosListWithoutFilter(3);

        Mockito.when(genreService.getGenres())
                .thenReturn(expectedResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/monolith/genres"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Drama"));
    }

    @Test
    public void shouldUpdateGenreNameById() throws Exception {
        GenreResponseDto expectedResponseDto =
                GenreTestUtils.generateGenreResponseDto(1, "Fantasy");

        Mockito.when(genreService.updateGenreNameById(expectedResponseDto.getId(), expectedResponseDto.getName()))
                .thenReturn(expectedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/monolith/genres/{id}", expectedResponseDto.getId())
                        .param("name", expectedResponseDto.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponseDto.getName()));
    }

    @Test
    public void shouldDeleteGenreById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/monolith/genres/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
