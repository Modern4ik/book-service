package com.library.book_service.mappers;

import com.library.book_service.dto.genre.GenreRequestCreateDto;
import com.library.book_service.dto.genre.GenreResponseDto;
import com.library.book_service.entity.Genre;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreResponseDto toDto(Genre genre);

    Genre toEntity(GenreRequestCreateDto genre);

    List<GenreResponseDto> mapToDto(List<Genre> genres);
}
