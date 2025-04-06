package com.booksAPI.booksAPI.repository.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String bookName;
    private String authorName;
    private Integer publicationYear;
}
