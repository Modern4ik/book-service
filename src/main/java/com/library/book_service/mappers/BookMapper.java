package com.library.book_service.mappers;

import com.library.book_service.dto.book.BookRequestCreateDto;
import com.library.book_service.dto.book.BookResponseDto;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "genreNames", source = "genres")
    BookResponseDto toDto(Book book);

    Book toEntity(BookRequestCreateDto bookRequestCreateDto);

    List<BookResponseDto> mapToDto(List<Book> books);

    default List<String> mapGenresToNames(List<Genre> genres) {
        return genres == null ? List.of() :
                genres.stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList());
    }
}
