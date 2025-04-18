package com.books.holder.utils;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.entity.Author;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorTestUtils {

    public static AuthorRequestCreateDto generateAuthorCreateDto(String firstName, String lastName,
                                                                 LocalDate birthday, String country) {
        return new AuthorRequestCreateDto(firstName, lastName, birthday, country);
    }

    public static AuthorRequestDto generateAuthorFilterDto(String firstName, String lastName,
                                                           LocalDate birthday, String country) {
        return new AuthorRequestDto(firstName, lastName, birthday, country);
    }

    public static Author generateAuthor(Integer id, String firstName, String lastName,
                                        LocalDate birthday, String country) {
        return new Author(id, firstName, lastName, birthday, country, new ArrayList<>());
    }

    public static List<Author> generateAuthorsList(AuthorRequestDto requestDto, int count) {
        List<Author> authors = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            authors.add(generateAuthor(
                    i,
                    requestDto.firstName(),
                    requestDto.lastName(),
                    requestDto.birthday(),
                    requestDto.country()));
        }

        return authors;
    }

}
