package com.books.holder.dto.author;

import java.time.LocalDate;

public record AuthorRequestDto(String firstName,
                               String lastName,
                               LocalDate birthday,
                               String country) {
}
