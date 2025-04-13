package com.books.holder.mappers;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.entity.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponseDto toDto(Author author);
    Author toEntity(AuthorRequestCreateDto authorRequestCreateDto);

    List<AuthorResponseDto> mapToDto(List<Author> authors);
}
