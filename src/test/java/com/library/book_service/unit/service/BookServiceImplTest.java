package com.library.book_service.unit.service;

import com.library.book_service.dto.book.BookRequestCreateDto;
import com.library.book_service.dto.book.BookRequestFilterDto;
import com.library.book_service.dto.book.BookRequestUpdateDto;
import com.library.book_service.dto.book.BookResponseDto;
import com.library.book_service.entity.Author;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Genre;
import com.library.book_service.mappers.BookMapperImpl;
import com.library.book_service.repository.AuthorRepository;
import com.library.book_service.repository.BookRepository;
import com.library.book_service.repository.GenreRepository;
import com.library.book_service.service.book.BookServiceImpl;
import com.library.book_service.service.cache.CacheVersionService;
import com.library.book_service.specifications.BookSpecification;
import com.library.book_service.utils.AuthorTestUtils;
import com.library.book_service.utils.BookTestUtils;
import com.library.book_service.utils.GenreTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookSpecification bookSpecification;
    @Spy
    private CacheVersionService cacheVersionService;
    @Spy
    private BookMapperImpl bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void shouldSaveNewBook() {
        BookRequestCreateDto createDto =
                BookTestUtils.generateBookCreateDto("TestBook", 1, List.of(1, 2), 2000);
        Author expectedAuthor = AuthorTestUtils.generateAuthor(1, "Unknown", null, null, null);
        Genre expectedDramaGenre = GenreTestUtils.generateGenre(1, "Drama", new ArrayList<>());
        Genre expectedFantasyGenre = GenreTestUtils.generateGenre(2, "Fantasy", new ArrayList<>());

        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(expectedAuthor));
        Mockito.when(genreRepository.findById(1)).thenReturn(Optional.of(expectedDramaGenre));
        Mockito.when(genreRepository.findById(2)).thenReturn(Optional.of(expectedFantasyGenre));

        bookService.saveBook(createDto);
        Assertions.assertEquals(1, expectedAuthor.getBooks().size());
        Assertions.assertEquals(createDto.bookName(), expectedAuthor.getBooks().get(0).getBookName());
        Assertions.assertEquals(2, expectedAuthor.getBooks().get(0).getGenres().size());
        Assertions.assertTrue(expectedAuthor.getBooks().get(0).getGenres().contains(expectedDramaGenre));
        Assertions.assertTrue(expectedAuthor.getBooks().get(0).getGenres().contains(expectedFantasyGenre));
    }

    @Test
    public void shouldReturnBookById() {
        List<Genre> genres = GenreTestUtils.generateGenresListWithoutFilter(2);

        Mockito.when(bookRepository.findById(2L))
                .thenReturn(Optional.of(BookTestUtils.generateBook(2L, "Founded Book", 1, genres, null)));

        BookResponseDto responseDto = bookService.getBookById(2L);
        Assertions.assertEquals(2L, responseDto.getId());
        Assertions.assertEquals("Founded Book", responseDto.getBookName());
        Assertions.assertEquals(1, responseDto.getAuthorId());
        Assertions.assertEquals(genres.size(), responseDto.getGenreNames().size());
        Assertions.assertTrue(responseDto.getGenreNames().contains(genres.get(0).getName()));
    }

    @Test
    public void shouldReturnBooksByNameAndAuthorId() {
        BookRequestFilterDto filterDto =
                BookTestUtils.generateBookFilterDto("Filter Book", 3, null, null);
        List<Book> expectedBooks = BookTestUtils.generateBooksList(filterDto, 3);

        Mockito.when(bookRepository.findAll(bookSpecification.generateBookSpec(filterDto)))
                .thenReturn(expectedBooks);

        List<BookResponseDto> response = bookService.getBooks(filterDto);
        Assertions.assertEquals(3, response.size());
        for (Book book : expectedBooks) {
            Assertions.assertEquals("Filter Book", book.getBookName());
            Assertions.assertEquals(3, book.getAuthor().getId());
        }
    }

    @Test
    public void shouldUpdateBookById() {
        List<Genre> expectedGenres = GenreTestUtils.generateGenresListWithoutFilter(1);
        List<Integer> newGenresId = new ArrayList<>(List.of(2, 3));

        BookRequestUpdateDto updateDto = BookTestUtils.generateBookUpdateDto("Updated Book", 99, newGenresId, 1999);
        Author expectedNewAuthor = AuthorTestUtils.generateAuthor(99, "New Author", null, null, null);
        Book expectedBookToUpdate = BookTestUtils.generateBook(1L, "First Book", 1, expectedGenres, 2005);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBookToUpdate));
        Mockito.when(authorRepository.findById(99)).thenReturn(Optional.of(expectedNewAuthor));
        Mockito.when(genreRepository.findById(2)).thenReturn(Optional.of(GenreTestUtils.generateGenre(2, "Horror", new ArrayList<>())));
        Mockito.when(genreRepository.findById(3)).thenReturn(Optional.of(GenreTestUtils.generateGenre(3, "Action", new ArrayList<>())));

        bookService.updateBookById(1L, updateDto);
        Assertions.assertEquals(updateDto.bookName(), expectedBookToUpdate.getBookName());
        Assertions.assertEquals(updateDto.authorId(), expectedBookToUpdate.getAuthor().getId());
        Assertions.assertEquals(3, expectedBookToUpdate.getGenres().size());
        Assertions.assertEquals("Horror", expectedBookToUpdate.getGenres().get(1).getName());
        Assertions.assertEquals(updateDto.publicationYear(), expectedBookToUpdate.getPublicationYear());
    }

    @Test
    public void shouldDeleteBookById() {
        bookService.deleteBookById(1L);
        Mockito.verify(bookRepository).deleteById(1L);
    }
}
