package com.books.holder.dto.author;

import java.sql.Date;

public record AuthorCreateDto(String firstName,
                              String lastName,
                              Date birthday,
                              String country) {
}
