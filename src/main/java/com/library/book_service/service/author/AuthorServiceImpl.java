package com.library.book_service.service.author;

import com.library.book_service.constants.MessageTemplates;
import com.library.book_service.dto.author.AuthorRequestCreateDto;
import com.library.book_service.dto.author.AuthorRequestFilterDto;
import com.library.book_service.dto.author.AuthorResponseDto;
import com.library.book_service.mappers.AuthorMapper;
import com.library.book_service.repository.AuthorRepository;
import com.library.book_service.service.cache.CacheVersionService;
import com.library.book_service.specifications.AuthorSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private static final int UNKNOWN_AUTHOR_ID = 1;
    private static final String CACHE_NAMESPACE = "authors";

    private final AuthorRepository authorRepository;
    private final AuthorSpecification authorSpecification;
    private final AuthorMapper authorMapper;
    private final CacheVersionService cacheVersionService;

    @Override
    @Transactional
    public AuthorResponseDto saveAuthor(AuthorRequestCreateDto authorRequestCreateDto) {
        AuthorResponseDto newAuthorDto = authorMapper.toDto(
                authorRepository.save(authorMapper.toEntity(authorRequestCreateDto)));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return newAuthorDto;
    }

    @Override
    @Cacheable(value = "authorById", key = "#id")
    public AuthorResponseDto getAuthorById(Integer id) {
        return authorMapper.toDto(authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.AUTHOR_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    @Cacheable(value = "authorsByFilter", key = "{#authorRequestFilterDto, @cacheVersionService.getCurrentVersion('authors')}")
    public List<AuthorResponseDto> getAuthors(AuthorRequestFilterDto authorRequestFilterDto) {
        return authorMapper.mapToDto(authorRepository.findAll(
                authorSpecification.generateAuthorSpec(authorRequestFilterDto)
        ));
    }

    @Override
    @CacheEvict(value = "authorById", key = "#id")
    public void deleteAuthorById(Integer id) {
        if (id == UNKNOWN_AUTHOR_ID) {
            throw new IllegalStateException(MessageTemplates.BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE);
        }

        authorRepository.deleteById(id);
        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

}
