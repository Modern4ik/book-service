package com.books.holder.service;

import com.books.holder.dto.book.BookRequestCreateDto;
import com.books.holder.dto.book.BookRequestDto;
import com.books.holder.dto.book.BookRequestUpdateDto;
import com.books.holder.entity.Author;
import com.books.holder.repository.AuthorRepository;
import com.books.holder.repository.BookRepository;
import com.books.holder.entity.Book;
import com.books.holder.dto.book.BookResponseDto;
import com.books.holder.mappers.BookMapper;
import com.books.holder.specifications.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";
    private static final String BOOK_NOT_FOUND_MESSAGE = "Book with ID = %d not found!";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final BookSpecification bookSpecification;

    @Transactional
    public void saveBook(BookRequestCreateDto bookRequestCreateDto) {
        int authorId = bookRequestCreateDto.authorId();

        Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));

        foundedAuthor.addBook(bookMapper.toEntity(bookRequestCreateDto));
    }

    public BookResponseDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(id))));
    }

    public List<BookResponseDto> getBooks(BookRequestDto bookRequestDto) {
        return bookMapper.mapToDto(bookRepository.findAll(
                bookSpecification.generateBookSpec(bookRequestDto)
        ));
    }

    @Transactional
    public void updateBookById(Long id, BookRequestUpdateDto bookRequestUpdateDto) {
        Book bookWithId = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(id)));

        updateBook(bookWithId, bookRequestUpdateDto);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBook(Book currBook, BookRequestUpdateDto bookRequestUpdateDto) {
        String bookName = bookRequestUpdateDto.bookName();
        Integer authorId = bookRequestUpdateDto.authorId();
        Integer publicationYear = bookRequestUpdateDto.publicationYear();

        if (isCanSetBookName(currBook, bookName)) {
            currBook.setBookName(bookName);
        }

        if (isCanSetAuthor(currBook, authorId)) {
            Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                    new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));

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
        return newAuthor != null && !currBook.getAuthor().getId().equals(newAuthor);
    }

    private boolean isCanSetPublicationYear(Book currBook, Integer newPublicationYear) {
        return currBook.getPublicationYear() == null
                || newPublicationYear != null && !currBook.getPublicationYear().equals(newPublicationYear);
    }

}
