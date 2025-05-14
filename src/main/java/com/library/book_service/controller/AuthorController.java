package com.library.book_service.controller;

import com.library.book_service.dto.author.AuthorRequestCreateDto;
import com.library.book_service.dto.author.AuthorRequestFilterDto;
import com.library.book_service.dto.author.AuthorResponseDto;
import com.library.book_service.service.author.AuthorService;
import com.library.book_service.validation.annotations.NotOne;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monolith/authors")
@RequiredArgsConstructor
@Validated
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto saveAuthor(@RequestBody @Valid AuthorRequestCreateDto authorRequestCreateDto) {
        return authorService.saveAuthor(authorRequestCreateDto);
    }

    @GetMapping(path = "/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable Integer id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping
    public List<AuthorResponseDto> getAuthors(@RequestBody AuthorRequestFilterDto authorRequestFilterDto) {
        return authorService.getAuthors(authorRequestFilterDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthorById(@PathVariable @NotOne Integer id) {
        authorService.deleteAuthorById(id);
    }

}
