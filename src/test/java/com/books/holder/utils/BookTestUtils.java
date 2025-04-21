package com.books.holder.utils;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookTestUtils {

    public static BookRequestCreateDto generateBookCreateDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestCreateDto(bookName, authorId, publicationYear);
    }

    public static BookRequestFilterDto generateBookFilterDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestFilterDto(bookName, authorId, publicationYear);
    }

    public static BookRequestUpdateDto generateBookUpdateDto(String bookName, Integer authorId, Integer publicationYear) {
        return new BookRequestUpdateDto(bookName, authorId, publicationYear);
    }

    public static BookResponseDto generateBookResponseDto(Long id, String bookName, Integer authorId, Integer publicationYear) {
        return new BookResponseDto(id, bookName, authorId, publicationYear);
    }

    public static Book generateBook(Long id, String bookName, Integer authorId, Integer publicationYear) {
        return new Book(id,
                bookName,
                AuthorTestUtils.generateAuthor(authorId, "Unknown", null, null, null),
                publicationYear);
    }

    public static List<Book> generateBooksList(BookRequestFilterDto requestDto, int count) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            books.add(generateBook((long) i, requestDto.bookName(), requestDto.authorId(), requestDto.publicationYear()));
        }

        return books;
    }

    public static List<BookResponseDto> generateBookResponseDtoList(BookRequestFilterDto requestDto, int count) {
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            bookResponseDtos.add(new BookResponseDto(
                    (long) i + 1,
                    requestDto.bookName(),
                    requestDto.authorId(),
                    requestDto.publicationYear()

            ));
        }

        return bookResponseDtos;
    }

}
