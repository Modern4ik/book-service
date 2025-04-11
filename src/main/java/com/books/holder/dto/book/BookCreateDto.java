package com.books.holder.dto.book;

public record BookCreateDto(String bookName,
                            Integer authorId,
                            Integer publicationYear) {
}