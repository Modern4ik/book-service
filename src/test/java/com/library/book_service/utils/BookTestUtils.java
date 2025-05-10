package com.library.book_service.utils;

import com.library.book_service.dto.book.BookRequestCreateDto;
import com.library.book_service.dto.book.BookRequestFilterDto;
import com.library.book_service.dto.book.BookRequestUpdateDto;
import com.library.book_service.dto.book.BookResponseDto;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class BookTestUtils {

    public static BookRequestCreateDto generateBookCreateDto(String bookName, Integer authorId,
                                                             List<Integer> genresId, Integer publicationYear) {
        return new BookRequestCreateDto(bookName, authorId, genresId, publicationYear);
    }

    public static BookRequestFilterDto generateBookFilterDto(String bookName, Integer authorId,
                                                             List<String> genreNames, Integer publicationYear) {
        return new BookRequestFilterDto(bookName, authorId, genreNames, publicationYear);
    }

    public static BookRequestUpdateDto generateBookUpdateDto(String bookName, Integer authorId,
                                                             List<Integer> genresId, Integer publicationYear) {
        return new BookRequestUpdateDto(bookName, authorId, genresId, publicationYear);
    }

    public static BookResponseDto generateBookResponseDto(Long id, String bookName, Integer authorId, List<String> genres, Integer publicationYear) {
        return new BookResponseDto(id, bookName, authorId, genres, publicationYear);
    }

    public static Book generateBook(Long id, String bookName, Integer authorId, List<Genre> genres, Integer publicationYear) {
        return new Book(
                id,
                bookName,
                AuthorTestUtils.generateAuthor(authorId, "Unknown", null, null, null),
                publicationYear,
                genres,
                new ArrayList<>());
    }

    public static List<Book> generateBooksList(BookRequestFilterDto requestDto, int count) {
        List<Book> books = new ArrayList<>();
        List<Genre> bookGenres = requestDto.genreNames() == null ? null :
                requestDto.genreNames().stream()
                        .map(name -> GenreTestUtils.generateGenre(1, name, null))
                        .toList();

        for (int i = 0; i < count; i++) {
            books.add(generateBook((long) i, requestDto.bookName(), requestDto.authorId(), bookGenres, requestDto.publicationYear()));
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
                    requestDto.genreNames(),
                    requestDto.publicationYear()

            ));
        }

        return bookResponseDtos;
    }

}
