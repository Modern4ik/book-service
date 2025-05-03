package com.books.holder.unit.service;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestFilterDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.entity.Author;
import com.books.holder.mappers.AuthorMapperImpl;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.service.author.AuthorServiceImpl;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.specifications.AuthorSpecification;
import com.books.holder.utils.AuthorTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorSpecification authorSpecification;
    @Spy
    private CacheVersionService cacheVersionService;
    @Spy
    private AuthorMapperImpl authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void shouldSaveNewAuthor() {
        AuthorRequestCreateDto createAuthorDto =
                AuthorTestUtils.generateAuthorCreateDto("Unknown", null, null, null);
        Author expectedNewAuthor =
                AuthorTestUtils.generateAuthor(
                        1, createAuthorDto.firstName(), createAuthorDto.lastName(), createAuthorDto.birthday(), createAuthorDto.country());

        Mockito.when(authorRepository.save(authorMapper.toEntity(createAuthorDto)))
                .thenReturn(expectedNewAuthor);

        AuthorResponseDto newAuthor = authorService.saveAuthor(createAuthorDto);
        Mockito.verify(authorRepository).save(authorMapper.toEntity(createAuthorDto));
        Assertions.assertEquals(expectedNewAuthor.getId(), newAuthor.getId());
        Assertions.assertEquals(expectedNewAuthor.getFirstName(), newAuthor.getFirstName());
    }

    @Test
    public void shouldReturnAuthorById() {
        Author expectedAuthor =
                AuthorTestUtils.generateAuthor(1, "Unknown", null, null, null);

        Mockito.when(authorRepository.findById(1))
                .thenReturn(Optional.of(expectedAuthor));

        AuthorResponseDto responseWithAuthor = authorService.getAuthorById(1);
        Assertions.assertNotNull(responseWithAuthor);
        Assertions.assertEquals(1, responseWithAuthor.getId());
        Assertions.assertEquals("Unknown", responseWithAuthor.getFirstName());
    }

    @Test
    public void shouldReturnAuthorsBySecondNameAndBirthday() {
        AuthorRequestFilterDto filterDto = AuthorTestUtils.generateAuthorFilterDto(
                null, "Zaytsev", LocalDate.parse("1993-11-10"), null);
        List<Author> expectedAuthors = AuthorTestUtils.generateAuthorsList(filterDto, 3);

        Mockito.when(authorRepository.findAll(authorSpecification.generateAuthorSpec(filterDto)))
                .thenReturn(expectedAuthors);

        List<AuthorResponseDto> responseWithAuthors = authorService.getAuthors(filterDto);
        Assertions.assertEquals(3, responseWithAuthors.size());
        for (AuthorResponseDto responseDto : responseWithAuthors) {
            Assertions.assertEquals("Zaytsev", responseDto.getLastName());
            Assertions.assertEquals("1993-11-10", responseDto.getBirthday().toString());
        }
    }

    @Test
    public void shouldDeleteAuthorById() {
        authorService.deleteAuthorById(3);
        Mockito.verify(authorRepository).deleteById(3);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToDeleteUnknownAuthor() {
        IllegalStateException deleteException = Assertions.assertThrows(IllegalStateException.class,
                () -> authorService.deleteAuthorById(1));

        Assertions.assertEquals("Cant delete base unknown author with ID = 1!", deleteException.getMessage());
    }

}
