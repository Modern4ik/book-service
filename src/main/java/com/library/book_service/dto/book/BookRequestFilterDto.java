package com.library.book_service.dto.book;

import java.util.List;

public record BookRequestFilterDto(String bookName,
                                   Integer authorId,
                                   List<String> genreNames,
                                   Integer publicationYear) {
}
