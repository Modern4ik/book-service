package com.books.holder.service.author;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto saveAuthor(AuthorRequestCreateDto authorRequestCreateDto);

    AuthorResponseDto getAuthorById(Integer id);

    List<AuthorResponseDto> getAuthors(AuthorRequestFilterDto authorRequestFilterDto);

    void deleteAuthorById(Integer id);

}
