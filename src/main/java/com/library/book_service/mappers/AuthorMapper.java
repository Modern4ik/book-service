package com.library.book_service.mappers;

import com.library.book_service.dto.author.AuthorRequestCreateDto;
import com.library.book_service.dto.author.AuthorResponseDto;
import com.library.book_service.entity.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponseDto toDto(Author author);
    Author toEntity(AuthorRequestCreateDto authorRequestCreateDto);

    List<AuthorResponseDto> mapToDto(List<Author> authors);
}
