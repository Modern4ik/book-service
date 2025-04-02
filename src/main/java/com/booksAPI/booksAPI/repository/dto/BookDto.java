package com.booksAPI.booksAPI.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String bookName;
    private String authorName;
    private Integer publicationYear;
}
