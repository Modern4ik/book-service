package com.booksAPI.booksAPI.service;

import com.booksAPI.booksAPI.repository.Book;
import com.booksAPI.booksAPI.repository.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    void saveNewBook(BookDto bookDto);

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    List<Book> getBooksByAuthorName(String authorName);

    List<Book> getBooksByBookName(String bookName);

    List<Book> getBooksByPublicationYear(Integer year);

    void updateBookById(BookDto bookDto);

    void deleteBookById(Long id);
}
