package com.books.holder.service.genre;

import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;

import java.util.List;

public interface GenreService {

    String CACHE_NAMESPACE = "genres";

    GenreResponseDto saveGenre(GenreRequestCreateDto genreRequestCreateDto);

    GenreResponseDto getGenreById(Integer id);

    GenreResponseDto getGenreByName(String name);

    List<GenreResponseDto> getGenres();

    GenreResponseDto updateGenreNameById(Integer id, String newName);

    void deleteGenreById(Integer id);

    boolean genreExists(String genreName);

}
