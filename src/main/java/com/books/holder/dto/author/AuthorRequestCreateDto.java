package com.books.holder.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorRequestCreateDto(@NotBlank @Size(min = 2, max = 100) String firstName,
                                     String lastName,
                                     LocalDate birthday,
                                     String country) {
}
