package com.books.holder.dto.book;

public record BookWithoutAuthorReadDto(Long id,
                                       String bookName,
                                       Integer publicationYear) {
}
