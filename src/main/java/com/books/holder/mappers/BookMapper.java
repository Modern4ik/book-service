package com.books.holder.mappers;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.entity.Book;
import com.books.holder.dto.book.BookResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookResponseDto toDto(Book book);

    Book toEntity(BookRequestCreateDto bookRequestCreateDto);

    List<BookResponseDto> mapToDto(List<Book> bookList);

}
