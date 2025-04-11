package com.books.holder.dto.book;

public record BookUpdateDto(String bookName,
                            Integer authorId,
                            Integer publicationYear) {
}