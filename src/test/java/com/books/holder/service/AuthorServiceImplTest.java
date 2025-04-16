package com.books.holder.service;

import com.books.holder.dto.author.AuthorRequestCreateDto;
import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.dto.author.AuthorResponseDto;
import com.books.holder.entity.Author;
import com.books.holder.mappers.AuthorMapper;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.specifications.AuthorSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorSpecification authorSpecification;
    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private List<Author> authorsDbStub;

    @BeforeEach
    public void setup() {
        authorsDbStub = generateAuthorsDbStub();
    }

    @Test
    public void shouldSaveNewAuthor() {
        AuthorRequestCreateDto createAuthorDto = new AuthorRequestCreateDto(
                "Sergey",
                "Zaytsev",
                Date.valueOf("1993-11-10"),
                "Russian Federation"
        );
        Author expectedNewAuthor = new Author(
                4,
                createAuthorDto.firstName(),
                createAuthorDto.lastName(),
                createAuthorDto.birthday(),
                createAuthorDto.country(),
                null);

        Mockito.when(authorMapper.toEntity(createAuthorDto)).thenReturn(expectedNewAuthor);
        Mockito.when(authorRepository.save(expectedNewAuthor)).thenAnswer(invocationOnMock -> {
            authorsDbStub.add(expectedNewAuthor);
            return null;
        });

        authorService.saveNewAuthor(createAuthorDto);
        Assertions.assertEquals(4, authorsDbStub.size());
        Assertions.assertEquals(4, authorsDbStub.get(authorsDbStub.size() - 1).getId());
        Assertions.assertEquals("Sergey", authorsDbStub.get(authorsDbStub.size() - 1).getFirstName());
    }

    @Test
    public void shouldReturnAuthorById() {
        Author expectedAuthor = authorsDbStub.get(0);
        AuthorResponseDto expectedResponseDto = new AuthorResponseDto(
                expectedAuthor.getId(),
                expectedAuthor.getFirstName(),
                expectedAuthor.getLastName(),
                expectedAuthor.getBirthday(),
                expectedAuthor.getCountry());

        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(expectedAuthor));
        Mockito.when(authorMapper.toDto(expectedAuthor)).thenReturn(expectedResponseDto);

        AuthorResponseDto foundedAuthor = authorService.getAuthorById(1);
        Assertions.assertNotNull(foundedAuthor);
        Assertions.assertEquals(1, foundedAuthor.id());
        Assertions.assertEquals("Unknown", foundedAuthor.firstName());
    }

    @Test
    public void shouldReturnAuthorsBySecondNameAndBirthday() {
        AuthorRequestDto filterDto = new AuthorRequestDto(
                null,
                "Ivanov",
                Date.valueOf("1995-04-15"),
                null);
        List<Author> expectedAuthors = authorsDbStub.subList(1, authorsDbStub.size());

        Mockito.when(authorMapper.mapToDto(expectedAuthors)).thenReturn(expectedAuthors.stream()
                .map(expectedAuthor -> new AuthorResponseDto(
                        expectedAuthor.getId(),
                        expectedAuthor.getFirstName(),
                        expectedAuthor.getLastName(),
                        expectedAuthor.getBirthday(),
                        expectedAuthor.getCountry()))
                .toList());
        Mockito.when(authorRepository.findAll(authorSpecification.generateAuthorSpec(filterDto)))
                .thenReturn(expectedAuthors);

        List<AuthorResponseDto> response = authorService.getAuthors(filterDto);
        Assertions.assertEquals(2, response.size());
        for (AuthorResponseDto responseDto : response) {
            Assertions.assertEquals("Ivanov", responseDto.lastName());
            Assertions.assertEquals(Date.valueOf("1995-04-15"), responseDto.birthday());
        }
    }

    @Test
    public void shouldDeleteAuthorById() {
        Mockito.doAnswer(invocationOnMock -> {
            authorsDbStub.remove(authorsDbStub.size() - 1);
            return null;
        }).when(authorRepository).deleteById(3);

        authorService.deleteAuthorById(3);
        Assertions.assertEquals(2, authorsDbStub.size());
        Assertions.assertEquals(2, authorsDbStub.get(authorsDbStub.size() - 1).getId());
    }

    @Test
    public void shouldThrowExceptionWhenTryingToDeleteUnknownAuthor() {
        IllegalStateException deleteException = Assertions.assertThrows(IllegalStateException.class,
                () -> authorService.deleteAuthorById(1));

        Assertions.assertEquals("Cant delete base unknown author!", deleteException.getMessage());
    }

    private List<Author> generateAuthorsDbStub() {
        List<Author> authorsStub = new ArrayList<>();

        authorsStub.add(new Author(
                1,
                "Unknown",
                null,
                null,
                null,
                null
        ));

        authorsStub.add(new Author(
                2,
                "Maxim",
                "Ivanov",
                Date.valueOf("1995-04-15"),
                "Russian Federation",
                null
        ));

        authorsStub.add(new Author(
                3,
                "Igor",
                "Ivanov",
                Date.valueOf("1995-04-15"),
                "Belarus",
                null
        ));

        return authorsStub;
    }
}
