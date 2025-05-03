package com.books.holder.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookRequestCreateDto(@NotBlank @Size(max = 100) String bookName,
                                   @NotNull Integer authorId,
                                   @NotEmpty List<Integer> genresId,
                                   Integer publicationYear) {
}