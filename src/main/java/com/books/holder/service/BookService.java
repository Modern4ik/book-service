package com.books.holder.service;

import com.books.holder.dto.book.BookCreateDto;
import com.books.holder.dto.book.BookFilterDto;
import com.books.holder.dto.book.BookReadDto;
import com.books.holder.dto.book.BookUpdateDto;

import java.util.List;

public interface BookService {

    void saveBook(BookCreateDto bookCreateDto);

    BookReadDto getBookById(Long id);

    List<BookReadDto> getBooks(BookFilterDto bookFilterDto);

    void updateBookById(Long id, BookUpdateDto bookUpdateDto);

    void deleteBookById(Long id);
}
