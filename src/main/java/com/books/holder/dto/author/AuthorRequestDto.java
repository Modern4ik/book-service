package com.books.holder.dto.author;

import java.sql.Date;

public record AuthorRequestDto(String firstName,
                               String lastName,
                               Date birthday,
                               String country) {
}
