package com.books.holder.mappers;

import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.entity.Genre;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreResponseDto toDto(Genre genre);

    Genre toEntity(GenreRequestCreateDto genre);

    List<GenreResponseDto> mapToDto(List<Genre> genres);
}
