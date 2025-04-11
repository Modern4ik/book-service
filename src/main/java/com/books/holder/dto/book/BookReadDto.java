package com.books.holder.dto.book;

import com.books.holder.dto.author.AuthorReadDto;

public record BookReadDto(Long id,
                          String bookName,
                          AuthorReadDto author,
                          Integer publicationYear) {
}
