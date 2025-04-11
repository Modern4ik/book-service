package com.books.holder.controller;

import com.books.holder.dto.book.BookCreateDto;
import com.books.holder.dto.book.BookFilterDto;
import com.books.holder.dto.book.BookReadDto;
import com.books.holder.dto.book.BookUpdateDto;
import com.books.holder.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public void saveBook(@RequestBody BookCreateDto bookCreateDto) {
        bookService.saveBook(bookCreateDto);
    }

    @GetMapping(path = "/{id}")
    public BookReadDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<BookReadDto> getBooks(@RequestBody BookFilterDto bookFilterDto) {
        return bookService.getBooks(bookFilterDto);
    }

    @PutMapping("/{id}")
    public void updateBookById(@PathVariable Long id, @RequestBody BookUpdateDto bookUpdateDto) {
        bookService.updateBookById(id, bookUpdateDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
