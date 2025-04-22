package com.books.holder.service;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.dto.book.BookRequestUpdateDto;

import java.util.List;

public interface BookService {

    BookResponseDto saveBook(BookRequestCreateDto bookRequestCreateDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> getBooks(BookRequestFilterDto bookRequestFilterDto);

    BookResponseDto updateBookById(Long id, BookRequestUpdateDto bookRequestUpdateDto);

    void deleteBookById(Long id);
}
