package com.books.holder.utils;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.entity.Author;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorTestUtils {

    public static AuthorRequestCreateDto generateAuthorCreateDto(String firstName, String lastName,
                                                                 LocalDate birthday, String country) {
        return new AuthorRequestCreateDto(firstName, lastName, birthday, country);
    }

    public static AuthorRequestFilterDto generateAuthorFilterDto(String firstName, String lastName,
                                                                 LocalDate birthday, String country) {
        return new AuthorRequestFilterDto(firstName, lastName, birthday, country);
    }

    public static AuthorResponseDto generateAuthorResponseDto(int id, String firstName, String lastName,
                                                              LocalDate birthday, String country) {
        return new AuthorResponseDto(id, firstName, lastName, birthday, country);
    }

    public static Author generateAuthor(int id, String firstName, String lastName, LocalDate birthday, String country) {
        return new Author(id, firstName, lastName, birthday, country, new ArrayList<>());
    }

    public static List<Author> generateAuthorsList(AuthorRequestFilterDto requestDto, int count) {
        List<Author> authors = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            authors.add(generateAuthor(
                    i + 1,
                    requestDto.firstName(),
                    requestDto.lastName(),
                    requestDto.birthday(),
                    requestDto.country()
            ));
        }

        return authors;
    }

    public static List<AuthorResponseDto> generateAuthorResponseDtoList(AuthorRequestFilterDto filerDto, int count) {
        List<AuthorResponseDto> authorResponseDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            authorResponseDtos.add(generateAuthorResponseDto(
                    i + 1,
                    filerDto.firstName(),
                    filerDto.lastName(),
                    filerDto.birthday(),
                    filerDto.country()
            ));
        }

        return authorResponseDtos;
    }

}
