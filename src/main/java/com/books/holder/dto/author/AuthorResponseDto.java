package com.books.holder.dto.author;

import java.sql.Date;

public record AuthorResponseDto(Integer id,
                                String firstName,
                                String lastName,
                                Date birthday,
                                String country) {
}
