package com.books.holder.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequestCreateDto(@NotBlank @Size(max = 100) String bookName,
                                   @NotNull Integer authorId,
                                   Integer publicationYear) {
}