package com.books.holder.service;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto saveNewAuthor(AuthorRequestCreateDto authorRequestCreateDto);

    AuthorResponseDto getAuthorById(Integer id);

    List<AuthorResponseDto> getAuthors(AuthorRequestDto authorRequestDto);

    void deleteAuthorById(Integer id);

}
