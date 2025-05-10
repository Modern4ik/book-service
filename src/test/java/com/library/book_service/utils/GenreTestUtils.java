package com.library.book_service.utils;

import com.library.book_service.dto.genre.GenreRequestCreateDto;
import com.library.book_service.dto.genre.GenreResponseDto;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreTestUtils {

    public static GenreRequestCreateDto generateGenreCreateDto(String name) {
        return new GenreRequestCreateDto(name);
    }

    public static Genre generateGenre(Integer id, String name, List<Book> books) {
        return new Genre(id, name, books);
    }

    public static GenreResponseDto generateGenreResponseDto(Integer id, String name) {
        return new GenreResponseDto(id, name);
    }

    public static List<Genre> generateGenresListWithoutFilter(int count) {
        List<Genre> genres = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            genres.add(generateGenre(i + 1, "Drama", new ArrayList<>()));
        }

        return genres;
    }

    public static List<GenreResponseDto> generateGenreResponseDtosListWithoutFilter(int count) {
        List<GenreResponseDto> genreResponseDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            genreResponseDtos.add(generateGenreResponseDto(i + 1, "Drama"));
        }

        return genreResponseDtos;
    }
}
