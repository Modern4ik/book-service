package com.library.book_service.service.book;

import com.library.book_service.dto.book.BookRequestCreateDto;
import com.library.book_service.dto.book.BookRequestFilterDto;
import com.library.book_service.dto.book.BookRequestUpdateDto;
import com.library.book_service.dto.book.BookResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto saveBook(BookRequestCreateDto bookRequestCreateDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> getBooks(BookRequestFilterDto bookRequestFilterDto);

    BookResponseDto updateBookById(Long id, BookRequestUpdateDto bookRequestUpdateDto);

    void deleteBookById(Long id);
}
