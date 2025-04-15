package com.books.holder.controller;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public void saveNewAuthor(@RequestBody @Valid AuthorRequestCreateDto authorRequestCreateDto) {
        authorService.saveNewAuthor(authorRequestCreateDto);
    }

    @GetMapping(path = "/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable Integer id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping
    public List<AuthorResponseDto> getAuthors(@RequestBody AuthorRequestDto authorRequestDto){
        return authorService.getAuthors(authorRequestDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAuthorById(@PathVariable Integer id) {
        authorService.deleteAuthorById(id);
    }

}
