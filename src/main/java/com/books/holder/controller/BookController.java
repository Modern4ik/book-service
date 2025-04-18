package com.books.holder.controller;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> saveBook(@RequestBody @Valid BookRequestCreateDto bookRequestCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.saveBook(bookRequestCreateDto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getBooks(@RequestBody BookRequestDto bookRequestDto) {
        return ResponseEntity.ok(bookService.getBooks(bookRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBookById(@PathVariable Long id, @RequestBody BookRequestUpdateDto bookRequestUpdateDto) {
        return ResponseEntity.ok(bookService.updateBookById(id, bookRequestUpdateDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
