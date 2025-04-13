package com.books.holder.dto.book;

public record BookResponseDto(Long id,
                              String bookName,
                              Integer authorId,
                              Integer publicationYear) {
}
