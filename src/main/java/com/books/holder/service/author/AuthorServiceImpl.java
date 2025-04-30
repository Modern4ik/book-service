package com.books.holder.service.author;

import com.books.holder.constants.MessageTemplates;
import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.mappers.AuthorMapper;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.specifications.AuthorSpecification;
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
    @Transactional
    @CacheEvict(value = "authorById", key = "#id")
    public void deleteAuthorById(Integer id) {
        if (id == UNKNOWN_AUTHOR_ID) {
            throw new IllegalStateException(MessageTemplates.BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE);
        }

        authorRepository.deleteById(id);
        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

}
