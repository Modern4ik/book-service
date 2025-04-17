package com.books.holder.dto.author;

import java.time.LocalDate;

public record AuthorResponseDto(Integer id,
                                String firstName,
                                String lastName,
                                LocalDate birthday,
                                String country) {
}
