package com.books.holder.unit.service;

import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.entity.Genre;
import com.books.holder.mappers.GenreMapperImpl;
import com.books.holder.repository.GenreRepository;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.service.genre.GenreServiceImpl;
import com.books.holder.utils.GenreTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;
    @Spy
    private CacheVersionService cacheVersionService;
    @Spy
    private GenreMapperImpl genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    public void shouldSaveGenre() {
        GenreRequestCreateDto createDto = GenreTestUtils.generateGenreCreateDto("Drama");
        Genre expectedGenre = GenreTestUtils.generateGenre(1, createDto.name(), new ArrayList<>());

        Mockito.when(genreRepository.save(genreMapper.toEntity(createDto)))
                .thenReturn(expectedGenre);

        GenreResponseDto responseDto = genreService.saveGenre(createDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1, responseDto.getId());
        Assertions.assertEquals(createDto.name(), responseDto.getName());
    }

    @Test
    public void shouldReturnGenreById() {
        Genre expectedGenre = GenreTestUtils.generateGenre(1, "Drama", new ArrayList<>());

        Mockito.when(genreRepository.findById(1)).thenReturn(Optional.of(expectedGenre));

        GenreResponseDto responseDto = genreService.getGenreById(1);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedGenre.getId(), responseDto.getId());
        Assertions.assertEquals(expectedGenre.getName(), responseDto.getName());
    }

    @Test
    public void shouldReturnGenreByName() {
        Genre expectedGenre = GenreTestUtils.generateGenre(1, "Drama", new ArrayList<>());

        Mockito.when(genreRepository.findByNameIgnoreCase("Drama")).thenReturn(Optional.of(expectedGenre));

        GenreResponseDto responseDto = genreService.getGenreByName("Drama");
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedGenre.getId(), responseDto.getId());
        Assertions.assertEquals(expectedGenre.getName(), responseDto.getName());
    }

    @Test
    public void shouldReturnAllGenres() {
        List<Genre> expectedGenres = GenreTestUtils.generateGenresListWithoutFilter(3);

        Mockito.when(genreRepository.findAll()).thenReturn(expectedGenres);

        List<GenreResponseDto> responseDtoList = genreService.getGenres();
        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(expectedGenres.size(), responseDtoList.size());
        Assertions.assertEquals(expectedGenres.get(0).getName(), responseDtoList.get(0).getName());
    }

    @Test
    public void shouldUpdateGenreNameById() {
        Genre expectedGenre = GenreTestUtils.generateGenre(1, "Drama", new ArrayList<>());

        Mockito.when(genreRepository.findById(1)).thenReturn(Optional.of(expectedGenre));

        GenreResponseDto responseDto = genreService.updateGenreNameById(1, "Fantasy");
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1, responseDto.getId());
        Assertions.assertEquals("Fantasy", responseDto.getName());
    }

    @Test
    public void shouldDeleteGenreById() {
        genreRepository.deleteById(1);

        Mockito.verify(genreRepository).deleteById(1);
    }
}
