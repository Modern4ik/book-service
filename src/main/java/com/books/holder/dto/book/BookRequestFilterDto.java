package com.books.holder.dto.book;

public record BookRequestFilterDto(String bookName,
                                   Integer authorId,
                                   Integer publicationYear) {
}
