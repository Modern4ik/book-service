package com.books.holder.dto.book;

public record BookRequestDto(String bookName,
                             Integer authorId,
                             Integer publicationYear) {
}
