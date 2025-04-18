package com.books.holder.controller;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.service.AuthorService;
import com.books.holder.validation.annotations.NotOne;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Validated
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> saveAuthor(@RequestBody @Valid AuthorRequestCreateDto authorRequestCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.saveNewAuthor(authorRequestCreateDto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Integer id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAuthors(@RequestBody AuthorRequestDto authorRequestDto) {
        return ResponseEntity.ok(authorService.getAuthors(authorRequestDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable @NotOne Integer id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }

}
