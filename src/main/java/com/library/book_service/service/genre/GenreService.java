package com.library.book_service.service.genre;

import com.library.book_service.dto.genre.GenreRequestCreateDto;
import com.library.book_service.dto.genre.GenreResponseDto;

import java.util.List;

public interface GenreService {

    GenreResponseDto saveGenre(GenreRequestCreateDto genreRequestCreateDto);

    GenreResponseDto getGenreById(Integer id);

    GenreResponseDto getGenreByName(String name);

    List<GenreResponseDto> getGenres();

    GenreResponseDto updateGenreNameById(Integer id, String newName);

    void deleteGenreById(Integer id);

    boolean genreExists(String genreName);

}
