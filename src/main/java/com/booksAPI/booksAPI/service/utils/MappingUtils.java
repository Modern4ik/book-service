package com.booksAPI.booksAPI.service.utils;

import com.booksAPI.booksAPI.repository.Book;
import com.booksAPI.booksAPI.repository.dto.BookDto;
import org.springframework.stereotype.Service;

@Service
public class MappingUtils {

    public Book mapToBookEntity(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getBookName(),
                bookDto.getAuthorName(), bookDto.getPublicationYear());
    }

    public BookDto mapToBookDto(Book book) {
        return new BookDto(book.getId(), book.getBookName(),
                book.getAuthorName(), book.getPublicationYear());
    }
}
