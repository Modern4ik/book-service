package com.books.holder.service;

import com.books.holder.dto.author.AuthorCreateDto;
import com.books.holder.dto.author.AuthorFilterDto;
import com.books.holder.dto.author.AuthorReadDto;
import com.books.holder.dto.book.BookWithoutAuthorReadDto;

import java.util.List;

public interface AuthorService {

    void saveNewAuthor(AuthorCreateDto authorCreateDto);

    AuthorReadDto getAuthorById(Integer id);

    List<AuthorReadDto> getAuthors(AuthorFilterDto authorFilterDto);

    List<BookWithoutAuthorReadDto> getAuthorBooks(Integer id);

    void deleteAuthorById(Integer id);

}
