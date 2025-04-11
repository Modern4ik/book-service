package com.books.holder.dto.author;

import java.sql.Date;

public record AuthorReadDto(Integer id,
                            String firstName,
                            String lastName,
                            Date birthday,
                            String country) {
}
