package com.books.holder.service;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.mappers.AuthorMapper;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.specifications.AuthorSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private static final int UNKNOWN_AUTHOR_ID = 1;
    private static final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";
    private static final String BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE = "Cant delete base unknown author with ID = 1!";

    private final AuthorRepository authorRepository;
    private final AuthorSpecification authorSpecification;
    private final AuthorMapper authorMapper;

    public AuthorResponseDto saveNewAuthor(AuthorRequestCreateDto authorRequestCreateDto) {
        return authorMapper.toDto(
                authorRepository.save(authorMapper.toEntity(authorRequestCreateDto)));
    }

    public AuthorResponseDto getAuthorById(Integer id) {
        return authorMapper.toDto(authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(id))));
    }

    public List<AuthorResponseDto> getAuthors(AuthorRequestDto authorRequestDto) {
        return authorMapper.mapToDto(authorRepository.findAll(
                authorSpecification.generateAuthorSpec(authorRequestDto)
        ));
    }

    public void deleteAuthorById(Integer id) {
        if (id == UNKNOWN_AUTHOR_ID) {
            throw new IllegalStateException(BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE);
        }
        authorRepository.deleteById(id);
    }

}
