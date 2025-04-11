package com.books.holder.service;

import com.books.holder.dto.book.BookCreateDto;
import com.books.holder.dto.book.BookFilterDto;
import com.books.holder.dto.book.BookUpdateDto;
import com.books.holder.repository.Author;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.repository.BookRepository;
import com.books.holder.repository.Book;
import com.books.holder.dto.book.BookReadDto;
import com.books.holder.mappers.BookMapper;
import com.books.holder.specifications.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final int UNKNOWN_AUTHOR_ID = 1;
    private final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";
    private final String BOOK_NOT_FOUND_MESSAGE = "Book with ID = %d not found!";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final BookSpecification bookSpecification;

    public void saveBook(BookCreateDto bookCreateDto) {
        int authorId;

        if (bookCreateDto.authorId() == null) {
            authorId = UNKNOWN_AUTHOR_ID;
        } else {
            authorId = bookCreateDto.authorId();
        }

        Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));

        foundedAuthor.addBook(bookRepository.save(bookMapper.toEntity(bookCreateDto)));
    }

    public BookReadDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(id))));
    }

    public List<BookReadDto> getBooks(BookFilterDto bookFilterDto) {
        return bookMapper.mapToReadDto(bookRepository.findAll(
                generateBookSpec(bookFilterDto)
        ));
    }

    public void updateBookById(Long id, BookUpdateDto bookUpdateDto) {
        Book bookWithId = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(id)));

        updateBook(bookWithId, bookUpdateDto);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBook(Book currBook, BookUpdateDto bookUpdateDto) {
        String bookName = bookUpdateDto.bookName();
        Integer authorId = bookUpdateDto.authorId();
        Integer publicationYear = bookUpdateDto.publicationYear();

        if (isCanSetBookName(currBook, bookName)) {
            currBook.setBookName(bookName);
        }

        if (isCanSetAuthor(currBook, authorId)) {
            Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                    new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));

            currBook.getAuthor().removeBook(currBook);
            foundedAuthor.addBook(currBook);
        }

        if (isCanSetPublicationYear(currBook, publicationYear)) {
            currBook.setPublicationYear(publicationYear);
        }
    }

    private boolean isCanSetBookName(Book currentBook, String newBookName) {
        return currentBook.getBookName() == null
                || newBookName != null && !currentBook.getBookName().equals(newBookName);
    }

    private boolean isCanSetAuthor(Book currBook, Integer newAuthor) {
        return !currBook.getAuthor().getId().equals(newAuthor);
    }

    private boolean isCanSetPublicationYear(Book currBook, Integer newPublicationYear) {
        return currBook.getPublicationYear() == null
                || newPublicationYear != null && !currBook.getPublicationYear().equals(newPublicationYear);
    }

    private Specification<Book> generateBookSpec(BookFilterDto bookFilterDto) {
        Specification<Book> spec = Specification.where(null);

        String bookName = bookFilterDto.bookName();
        Integer authorId = bookFilterDto.authorId();
        Integer publicationYear = bookFilterDto.publicationYear();

        if (bookName != null && !bookName.isEmpty()) {
            spec = spec.and(bookSpecification.hasBookName(bookName));
        }

        if (authorId != null) {
            spec = spec.and(bookSpecification.hasAuthor(
                    authorRepository.findById(authorId).orElseThrow(() ->
                            new EntityNotFoundException(
                                    AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)))));
        }

        if (publicationYear != null) {
            spec = spec.and(bookSpecification.hasPublicationYear(publicationYear));
        }

        return spec;
    }
}
