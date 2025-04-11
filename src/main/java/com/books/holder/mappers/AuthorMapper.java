package com.books.holder.mappers;

import com.books.holder.dto.author.AuthorCreateDto;
import com.books.holder.dto.author.AuthorReadDto;
import com.books.holder.repository.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorReadDto toReadDto(Author author);
    Author toEntity(AuthorCreateDto authorCreateDto);

    List<AuthorReadDto> mapToReadDto(List<Author> authorList);
}
