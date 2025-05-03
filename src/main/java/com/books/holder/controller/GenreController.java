package com.books.holder.controller;

import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.service.genre.GenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Validated
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponseDto saveGenre(@RequestBody @Valid GenreRequestCreateDto genreRequestCreateDto) {
        return genreService.saveGenre(genreRequestCreateDto);
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenreById(@PathVariable @NotNull Integer id) {
        return genreService.getGenreById(id);
    }

    @GetMapping("/by-name/{name}")
    public GenreResponseDto getGenreByName(@PathVariable @NotBlank String name) {
        return genreService.getGenreByName(name);
    }

    @GetMapping
    public List<GenreResponseDto> getGenres() {
        return genreService.getGenres();
    }

    @PatchMapping(path = "/{id}")
    public GenreResponseDto updateGenreNameById(@PathVariable @NotNull Integer id,
                                                @RequestParam @NotBlank String name) {
        return genreService.updateGenreNameById(id, name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenreById(@PathVariable @NotNull Integer id) {
        genreService.deleteGenreById(id);
    }

}
