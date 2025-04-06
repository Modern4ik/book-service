package com.books.holder.mappers;

import com.books.holder.repository.Book;
import com.books.holder.dto.BookDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);

    List<BookDto> mapToDto(List<Book> bookList);
    List<Book> mapToBook(List<BookDto> bookDtoList);

}
