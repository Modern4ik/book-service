package com.books.holder.dto.book;

public record BookRequestUpdateDto(String bookName,
                                   Integer authorId,
                                   Integer publicationYear) {
}