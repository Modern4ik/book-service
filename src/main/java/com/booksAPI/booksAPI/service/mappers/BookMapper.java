package com.booksAPI.booksAPI.service.mappers;

import com.booksAPI.booksAPI.repository.Book;
import com.booksAPI.booksAPI.repository.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);

    List<BookDto> mapToDto(List<Book> bookList);
    List<Book> mapToBook(List<BookDto> bookDtoList);

}
