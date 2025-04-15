package com.books.holder.controller;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public void saveBook(@RequestBody @Valid BookRequestCreateDto bookRequestCreateDto) {
        bookService.saveBook(bookRequestCreateDto);
    }

    @GetMapping(path = "/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<BookResponseDto> getBooks(@RequestBody BookRequestDto bookRequestDto) {
        return bookService.getBooks(bookRequestDto);
    }

    @PutMapping("/{id}")
    public void updateBookById(@PathVariable Long id, @RequestBody BookRequestUpdateDto bookRequestUpdateDto) {
        bookService.updateBookById(id, bookRequestUpdateDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
