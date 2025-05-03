package com.books.holder.integration.full;

import com.books.holder.controller.GenreController;
import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.repository.GenreRepository;
import com.books.holder.utils.GenreTestUtils;
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
@Sql(scripts = "/test-genres-data.sql")
public class GenreIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GenreController genreController;
    @Autowired
    private GenreRepository genreRepository;

    @AfterEach
    public void resetSequence() {
        entityManager.createNativeQuery(
                "ALTER TABLE genres ALTER COLUMN id RESTART WITH 1"
        ).executeUpdate();
    }

    @Test
    public void shouldSaveGenre() {
        GenreRequestCreateDto createDto = GenreTestUtils.generateGenreCreateDto("Action");

        GenreResponseDto responseDto = genreController.saveGenre(createDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(4, responseDto.getId());
        Assertions.assertEquals(createDto.name(), responseDto.getName());

        Assertions.assertEquals(4, genreRepository.count());
    }

    @Test
    public void shouldReturnGenreById() {
        GenreResponseDto responseDto = genreController.getGenreById(1);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1, responseDto.getId());
        Assertions.assertEquals("Drama", responseDto.getName());
    }

    @Test
    public void shouldReturnGenreByName() {
        GenreResponseDto responseDto = genreController.getGenreByName("Fantasy");

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(2, responseDto.getId());
        Assertions.assertEquals("Fantasy", responseDto.getName());
    }

    @Test
    public void shouldReturnAllGenres() {
        List<GenreResponseDto> responseDtoList = genreController.getGenres();

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(3, responseDtoList.size());
        Assertions.assertEquals(3, responseDtoList.get(responseDtoList.size() - 1).getId());
        Assertions.assertEquals("Horror", responseDtoList.get(responseDtoList.size() - 1).getName());
    }

    @Test
    public void shouldUpdateGenreNameById() {
        GenreResponseDto responseDto = genreController.updateGenreNameById(1, "Action");

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1, responseDto.getId());
        Assertions.assertEquals("Action", responseDto.getName());
    }

    @Test
    public void shouldDeleteGenreById() {
        genreController.deleteGenreById(3);

        Assertions.assertFalse(genreRepository.existsById(3));
        Assertions.assertEquals(2, genreRepository.count());
    }

}
