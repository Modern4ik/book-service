package com.books.holder.dto.book;

public record BookFilterDto(String bookName,
                            Integer authorId,
                            Integer publicationYear) {
}
