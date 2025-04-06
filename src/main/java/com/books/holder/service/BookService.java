package com.books.holder.service;

import com.books.holder.dto.BookDto;

import java.util.List;

public interface BookService {

    void saveNewBook(BookDto bookDto);

    List<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    List<BookDto> getBooksByAuthorName(String authorName);

    List<BookDto> getBooksByBookName(String bookName);

    List<BookDto> getBooksByPublicationYear(Integer year);

    void updateBookById(BookDto bookDto);

    void deleteBookById(Long id);
}
