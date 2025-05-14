package com.library.book_service.service.author;

import com.library.book_service.dto.author.AuthorRequestCreateDto;
import com.library.book_service.dto.author.AuthorRequestFilterDto;
import com.library.book_service.dto.author.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto saveAuthor(AuthorRequestCreateDto authorRequestCreateDto);

    AuthorResponseDto getAuthorById(Integer id);

    List<AuthorResponseDto> getAuthors(AuthorRequestFilterDto authorRequestFilterDto);

    void deleteAuthorById(Integer id);

}
