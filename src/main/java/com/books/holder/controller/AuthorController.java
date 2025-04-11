package com.books.holder.controller;

import com.books.holder.dto.author.AuthorCreateDto;
import com.books.holder.dto.author.AuthorFilterDto;
import com.books.holder.dto.author.AuthorReadDto;
import com.books.holder.dto.book.BookWithoutAuthorReadDto;
import com.books.holder.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public void saveNewAuthor(@RequestBody AuthorCreateDto authorCreateDto) {
        authorService.saveNewAuthor(authorCreateDto);
    }

    @GetMapping(path = "/{id}")
    public AuthorReadDto getAuthorById(@PathVariable Integer id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping
    public List<AuthorReadDto> getAuthors(@RequestBody AuthorFilterDto authorFilterDto){
        return authorService.getAuthors(authorFilterDto);
    }

    @GetMapping(path = "/{id}/author-books")
    public List<BookWithoutAuthorReadDto> getAuthorBooks(@PathVariable Integer id) {
        return authorService.getAuthorBooks(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAuthorById(@PathVariable Integer id) {
        authorService.deleteAuthorById(id);
    }
}
