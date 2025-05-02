package com.books.holder.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

    private Long id;
    private String bookName;
    private Integer authorId;
    private List<String> genreNames;
    private Integer publicationYear;

}
