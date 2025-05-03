package com.books.holder.controller;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.service.author.AuthorService;
import com.books.holder.validation.annotations.NotOne;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
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
