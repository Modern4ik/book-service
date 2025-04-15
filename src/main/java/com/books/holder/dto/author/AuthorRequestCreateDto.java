package com.books.holder.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public record AuthorRequestCreateDto(@NotBlank @Size(min = 2, max = 100) String firstName,
                                     String lastName,
                                     Date birthday,
                                     String country) {
}
