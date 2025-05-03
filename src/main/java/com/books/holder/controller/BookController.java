package com.books.holder.controller;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.service.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto saveBook(@RequestBody @Valid BookRequestCreateDto bookRequestCreateDto) {
        return bookService.saveBook(bookRequestCreateDto);
    }

    @GetMapping(path = "/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<BookResponseDto> getBooks(@RequestBody BookRequestFilterDto bookRequestFilterDto) {
        return bookService.getBooks(bookRequestFilterDto);
    }

    @PutMapping("/{id}")
    public BookResponseDto updateBookById(@PathVariable Long id, @RequestBody BookRequestUpdateDto bookRequestUpdateDto) {
        return bookService.updateBookById(id, bookRequestUpdateDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
