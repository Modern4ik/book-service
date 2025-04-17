package com.books.holder.utils;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookTestUtils {

    public static BookRequestCreateDto generateBookCreateDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestCreateDto(bookName, authorId, publicationYear);
    }

    public static BookRequestDto generateBookFilterDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestDto(bookName, authorId, publicationYear);
    }

    public static BookRequestUpdateDto generateUpdateDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestUpdateDto(bookName, authorId, publicationYear);
    }

    public static Book generateBook(Long id, String bookName, Integer authorId, Integer publicationYear) {
        return new Book(id,
                bookName,
                AuthorTestUtils.generateAuthor(authorId, "Unknown", null, null, null),
                publicationYear);
    }

    public static List<Book> generateBooksList(BookRequestDto requestDto, int count) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            books.add(generateBook((long) i, requestDto.bookName(), requestDto.authorId(), requestDto.publicationYear()));
        }

        return books;
    }

}
