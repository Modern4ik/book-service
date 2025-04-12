package com.books.holder.dto.book;

public record BookRequestCreateDto(String bookName,
                                   Integer authorId,
                                   Integer publicationYear) {
}