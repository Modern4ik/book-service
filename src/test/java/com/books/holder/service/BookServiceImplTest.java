package com.books.holder.service;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.entity.Author;
import com.books.holder.entity.Book;
import com.books.holder.mappers.BookMapper;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.repository.BookRepository;
import com.books.holder.specifications.BookSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private BookMapper bookMapper;
    @Mock
    private BookSpecification bookSpecification;

    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> booksDbStub;

    @BeforeEach
    public void setup() {
        booksDbStub = generateBooksDbStub();
    }

    @Test
    public void shouldSaveNewBook() {
        BookRequestCreateDto createDto = new BookRequestCreateDto(
                "New Book",
                1,
                1830
        );
        Author expectedAuthor = new Author(
                1,
                "Unknown",
                null,
                null,
                null,
                new ArrayList<>()
        );
        Book expectedNewBook = new Book(
                4L,
                createDto.bookName(),
                new Author(1, "Unknown", null, null, null, null),
                createDto.publicationYear()
        );


        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(expectedAuthor));
        Mockito.when(bookMapper.toEntity(createDto)).thenAnswer(invocationOnMock -> {
            booksDbStub.add(expectedNewBook);
            return expectedNewBook;
        });

        bookService.saveBook(createDto);
        Assertions.assertEquals(4, booksDbStub.size());
        Assertions.assertEquals(4L, booksDbStub.get(booksDbStub.size() - 1).getId());
        Assertions.assertEquals(1, booksDbStub.get(booksDbStub.size() - 1).getAuthor().getId());
        Assertions.assertEquals(expectedNewBook.getAuthor(), expectedAuthor);
        Assertions.assertEquals(1, expectedAuthor.getBooks().size());
        Assertions.assertEquals(expectedNewBook, expectedAuthor.getBooks().get(0));
    }

    @Test
    public void shouldReturnBookById() {
        Book expectedBook = booksDbStub.get(1);
        BookResponseDto expectedDto = new BookResponseDto(
                expectedBook.getId(),
                expectedBook.getBookName(),
                expectedBook.getAuthor().getId(),
                expectedBook.getPublicationYear()
        );

        Mockito.when(bookRepository.findById(2L)).thenReturn(Optional.of(expectedBook));
        Mockito.when(bookMapper.toDto(expectedBook)).thenReturn(expectedDto);

        BookResponseDto responseDto = bookService.getBookById(2L);
        Assertions.assertEquals(2L, responseDto.id());
        Assertions.assertEquals("Second Book", responseDto.bookName());
        Assertions.assertEquals(1, responseDto.authorId());
    }

    @Test
    public void shouldReturnBooksByNameAndAuthorId() {
        BookRequestDto filterDto = new BookRequestDto(
                "First Book",
                1,
                null
        );
        List<Book> expectedBooks = booksDbStub.subList(0, 1);

        Mockito.when(bookRepository.findAll(bookSpecification.generateBookSpec(filterDto)))
                .thenReturn(expectedBooks);
        Mockito.when(bookMapper.mapToDto(expectedBooks)).thenReturn(expectedBooks.stream()
                .map(book -> new BookResponseDto(
                        book.getId(),
                        book.getBookName(),
                        book.getAuthor().getId(),
                        book.getPublicationYear()
                )).toList());

        List<BookResponseDto> response = bookService.getBooks(filterDto);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("First Book", response.get(0).bookName());
        Assertions.assertEquals(1, response.get(0).authorId());
    }

    @Test
    public void shouldUpdateBookById() {
        BookRequestUpdateDto updateDto = new BookRequestUpdateDto(
                "Updated Book",
                99,
                1999
        );
        Author expectedNewAuthor = new Author(
                99,
                "New Author",
                null,
                null,
                null,
                new ArrayList<>()
        );
        Book expectedBookToUpdate = booksDbStub.get(0);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBookToUpdate));
        Mockito.when(authorRepository.findById(99)).thenReturn(Optional.of(expectedNewAuthor));

        bookService.updateBookById(1L, updateDto);
        Assertions.assertEquals("Updated Book", expectedBookToUpdate.getBookName());
        Assertions.assertEquals(99, expectedBookToUpdate.getAuthor().getId());
        Assertions.assertEquals(1999, expectedBookToUpdate.getPublicationYear());
    }

    @Test
    public void shouldDeleteBookById() {
        Mockito.doAnswer(invocationOnMock -> {
            booksDbStub.remove(0);
            return null;
        }).when(bookRepository).deleteById(1L);

        bookService.deleteBookById(1L);
        Assertions.assertEquals(2, booksDbStub.size());
        Assertions.assertEquals(2L, booksDbStub.get(0).getId());
        Assertions.assertEquals("Second Book", booksDbStub.get(0).getBookName());
    }

    private List<Book> generateBooksDbStub() {
        List<Book> booksStub = new ArrayList<>();
        Author authorStub = new Author(
                1,
                "Unknown",
                null,
                null,
                null,
                new ArrayList<>()
        );

        booksStub.add(new Book(
                1L,
                "First Book",
                authorStub,
                2000
        ));

        booksStub.add(new Book(
                2L,
                "Second Book",
                authorStub,
                1870
        ));

        booksStub.add(new Book(
                3L,
                "Third Book",
                authorStub,
                1925
        ));

        return booksStub;
    }
}
