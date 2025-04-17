package com.books.holder.service;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.entity.Author;
import com.books.holder.mappers.AuthorMapper;
import com.books.holder.mappers.AuthorMapperImpl;
import com.books.holder.repository.AuthorRepository;
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
    private AuthorMapper authorMapper = new AuthorMapperImpl();

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void shouldSaveNewAuthor() {
        AuthorRequestCreateDto createAuthorDto =
                AuthorTestUtils.generateAuthorCreateDto("Sergey", null, null, null);

        authorService.saveNewAuthor(createAuthorDto);
        Mockito.verify(authorRepository).save(authorMapper.toEntity(createAuthorDto));
    }

    @Test
    public void shouldReturnAuthorById() {
        Mockito.when(authorRepository.findById(1))
                .thenReturn(Optional.of(AuthorTestUtils.generateAuthor(1, "Unknown",
                        null, null, null)));

        AuthorResponseDto responseWithAuthor = authorService.getAuthorById(1);
        Assertions.assertNotNull(responseWithAuthor);
        Assertions.assertEquals(1, responseWithAuthor.id());
        Assertions.assertEquals("Unknown", responseWithAuthor.firstName());
    }

    @Test
    public void shouldReturnAuthorsBySecondNameAndBirthday() {
        AuthorRequestDto filterDto = AuthorTestUtils.generateAuthorFilterDto(
                null, "Zaytsev", LocalDate.parse("1993-11-10"), null);
        List<Author> expectedAuthors = AuthorTestUtils.generateAuthorsList(filterDto, 3);

        Mockito.when(authorRepository.findAll(authorSpecification.generateAuthorSpec(filterDto)))
                .thenReturn(expectedAuthors);

        List<AuthorResponseDto> responseWithAuthors = authorService.getAuthors(filterDto);
        Assertions.assertEquals(3, responseWithAuthors.size());
        for (AuthorResponseDto responseDto : responseWithAuthors) {
            Assertions.assertEquals("Zaytsev", responseDto.lastName());
            Assertions.assertEquals("1993-11-10", responseDto.birthday().toString());
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

        Assertions.assertEquals("Cant delete base unknown author!", deleteException.getMessage());
    }

}
