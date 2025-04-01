package com.booksAPI.booksAPI.controller;

import com.booksAPI.booksAPI.repository.LibBook;
import com.booksAPI.booksAPI.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public void saveNewBook(@RequestBody LibBook newBook) {
        bookService.saveNewBook(newBook);
    }

    @GetMapping
    public List<LibBook> readAllBooks() {
        return bookService.readAllBooks();
    }

    @GetMapping(path = "by-id/{id}")
    public LibBook readBookById(@PathVariable Long id) {
        return bookService.readBookById(id).orElse(null);
    }

    @GetMapping(path = "by-author/{authorName}")
    public List<LibBook> readBooksByAuthorName(@PathVariable String authorName) {
        return bookService.readBooksByAuthorName(authorName);
    }

    @GetMapping(path = "by-book-name/{bookName}")
    public List<LibBook> readBooksByBookName(@PathVariable String bookName) {
        return bookService.readBooksByBookName(bookName);
    }

    @GetMapping(path = "by-publication-year/{year}")
    public List<LibBook> readBooksByPublicationYear(@PathVariable Integer year) {
        return bookService.readBooksByPublicationYear(year);
    }

    @PutMapping(path = "{id}")
    public void updateBookById(@PathVariable Long id,
                               @RequestParam(required = false) String authorName,
                               @RequestParam(required = false) Integer publicationYear) {
        bookService.updateBookById(id, authorName, publicationYear);
    }

    @DeleteMapping(path = "{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
