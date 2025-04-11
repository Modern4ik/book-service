package com.books.holder.service;

import com.books.holder.dto.author.AuthorCreateDto;
import com.books.holder.dto.author.AuthorFilterDto;
import com.books.holder.dto.author.AuthorReadDto;
import com.books.holder.dto.book.BookWithoutAuthorReadDto;
import com.books.holder.mappers.AuthorMapper;
import com.books.holder.mappers.BookMapper;
import com.books.holder.repository.Author;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.specifications.AuthorSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final int UNKNOWN_AUTHOR_ID = 1;
    private final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";
    private final String BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE = "Cant delete base unknown author!";

    private final AuthorRepository authorRepository;
    private final AuthorSpecification authorSpecification;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public void saveNewAuthor(AuthorCreateDto authorCreateDto) {
        authorRepository.save(authorMapper.toEntity(authorCreateDto));
    }

    public AuthorReadDto getAuthorById(Integer id) {
        return authorMapper.toReadDto(authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(id))));
    }

    public List<AuthorReadDto> getAuthors(AuthorFilterDto authorFilterDto) {
        return authorMapper.mapToReadDto(authorRepository.findAll(
                generateAuthorSpec(authorFilterDto)
        ));
    }

    public List<BookWithoutAuthorReadDto> getAuthorBooks(Integer id) {
        Author foundedAuthor = authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(id)));

        return bookMapper.mapToReadDtoWithoutAuthor(foundedAuthor.getBooks());
    }

    public void deleteAuthorById(Integer id) {
        if (id == UNKNOWN_AUTHOR_ID) {
            throw new IllegalStateException(BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE);
        }
        authorRepository.deleteById(id);
    }

    private Specification<Author> generateAuthorSpec(AuthorFilterDto authorFilterDto) {
        Specification<Author> spec = Specification.where(null);

        if (authorFilterDto.firstName() != null && !authorFilterDto.firstName().isEmpty()) {
            spec = spec.and(authorSpecification.hasFirstName(authorFilterDto.firstName()));
        }

        if (authorFilterDto.lastName() != null && !authorFilterDto.lastName().isEmpty()) {
            spec = spec.and(authorSpecification.hasLastName(authorFilterDto.lastName()));
        }

        if (authorFilterDto.birthday() != null) {
            spec = spec.and(authorSpecification.hasBirthday(authorFilterDto.birthday()));
        }

        if (authorFilterDto.country() != null) {
            spec = spec.and(authorSpecification.hasCountry(authorFilterDto.country()));
        }

        return spec;
    }
}
