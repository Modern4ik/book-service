package com.books.holder.dto.book;

import java.util.List;

public record BookRequestUpdateDto(String bookName,
                                   Integer authorId,
                                   List<Integer> genresId,
                                   Integer publicationYear) {
}