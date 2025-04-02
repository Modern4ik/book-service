package com.booksAPI.booksAPI.controller;

import com.booksAPI.booksAPI.repository.Book;
import com.booksAPI.booksAPI.repository.dto.BookDto;
import com.booksAPI.booksAPI.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public void saveNewBook(@RequestBody BookDto bookDto) {
        bookService.saveNewBook(bookDto);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(path = "{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id).orElse(null);
    }

    @GetMapping(path = "by-author/{authorName}")
    public List<Book> getBooksByAuthorName(@PathVariable String authorName) {
        return bookService.getBooksByAuthorName(authorName);
    }

    @GetMapping(path = "by-book-name/{bookName}")
    public List<Book> getBooksByBookName(@PathVariable String bookName) {
        return bookService.getBooksByBookName(bookName);
    }

    @GetMapping(path = "by-publication-year/{year}")
    public List<Book> getBooksByPublicationYear(@PathVariable Integer year) {
        return bookService.getBooksByPublicationYear(year);
    }

    @PutMapping
    public void updateBookById(@RequestBody BookDto bookDto) {
        bookService.updateBookById(bookDto);
    }

    @DeleteMapping(path = "{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
