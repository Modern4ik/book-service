package com.books.holder.service;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.dto.book.BookRequestUpdateDto;

import java.util.List;

public interface BookService {

    void saveBook(BookRequestCreateDto bookRequestCreateDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> getBooks(BookRequestDto bookRequestDto);

    void updateBookById(Long id, BookRequestUpdateDto bookRequestUpdateDto);

    void deleteBookById(Long id);
}
