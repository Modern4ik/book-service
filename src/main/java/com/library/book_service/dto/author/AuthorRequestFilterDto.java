package com.library.book_service.dto.author;

import java.time.LocalDate;

public record AuthorRequestFilterDto(String firstName,
                                     String lastName,
                                     LocalDate birthday,
                                     String country) {
}
