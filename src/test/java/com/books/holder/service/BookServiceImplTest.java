package com.books.holder.service;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.entity.Author;
import com.books.holder.entity.Book;
import com.books.holder.mappers.BookMapper;
import com.books.holder.mappers.BookMapperImpl;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.repository.BookRepository;
import com.books.holder.specifications.BookSpecification;
import com.books.holder.utils.AuthorTestUtils;
import com.books.holder.utils.BookTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookSpecification bookSpecification;
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void shouldSaveNewBook() {
        BookRequestCreateDto createDto = BookTestUtils.generateBookCreateDto("New Book", 1, null);
        Author expectedAuthor = AuthorTestUtils.generateAuthor(1, "Unknown", null, null, null);

        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(expectedAuthor));

        bookService.saveBook(createDto);
        Assertions.assertEquals(1, expectedAuthor.getBooks().size());
        Assertions.assertEquals(createDto.bookName(), expectedAuthor.getBooks().get(0).getBookName());
    }

    @Test
    public void shouldReturnBookById() {
        Mockito.when(bookRepository.findById(2L))
                .thenReturn(Optional.of(BookTestUtils.generateBook(2L, "Founded Book", 1, null)));

        BookResponseDto responseDto = bookService.getBookById(2L);
        Assertions.assertEquals(2L, responseDto.id());
        Assertions.assertEquals("Founded Book", responseDto.bookName());
        Assertions.assertEquals(1, responseDto.authorId());
    }

    @Test
    public void shouldReturnBooksByNameAndAuthorId() {
        BookRequestDto filterDto = BookTestUtils.generateBookFilterDto("Filter Book", 3, null);
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
        BookRequestUpdateDto updateDto = BookTestUtils.generateUpdateDto("Updated Book", 99, 1999);
        Author expectedNewAuthor = AuthorTestUtils.generateAuthor(99, "New Author", null, null, null);
        Book expectedBookToUpdate = BookTestUtils.generateBook(1L, "First Book", 1780, null);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBookToUpdate));
        Mockito.when(authorRepository.findById(99)).thenReturn(Optional.of(expectedNewAuthor));

        bookService.updateBookById(1L, updateDto);
        Assertions.assertEquals("Updated Book", expectedBookToUpdate.getBookName());
        Assertions.assertEquals(99, expectedBookToUpdate.getAuthor().getId());
        Assertions.assertEquals(1999, expectedBookToUpdate.getPublicationYear());
    }

    @Test
    public void shouldDeleteBookById() {
        bookService.deleteBookById(1L);
        Mockito.verify(bookRepository).deleteById(1L);
    }
}
