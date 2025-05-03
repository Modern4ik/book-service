package com.books.holder.mappers;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.entity.Book;
import com.books.holder.entity.Genre;
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
