package com.books.holder.mappers;

import com.books.holder.dto.book.BookCreateDto;
import com.books.holder.dto.book.BookWithoutAuthorReadDto;
import com.books.holder.repository.Book;
import com.books.holder.dto.book.BookReadDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookReadDto toDto(Book book);
    Book toEntity(BookCreateDto bookCreateDto);

    List<BookReadDto> mapToReadDto(List<Book> bookList);
    List<BookWithoutAuthorReadDto> mapToReadDtoWithoutAuthor(List<Book> bookList);

}
