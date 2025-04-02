package com.booksAPI.booksAPI.service;

import com.booksAPI.booksAPI.repository.BookRepository;
import com.booksAPI.booksAPI.repository.Book;
import com.booksAPI.booksAPI.repository.dto.BookDto;
import com.booksAPI.booksAPI.service.utils.MappingUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MappingUtils mappingUtils;

    public void saveNewBook(BookDto bookDto) {
        bookRepository.save(mappingUtils.mapToBookEntity(bookDto));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        return bookRepository.findBooksByAuthorName(authorName);
    }

    public List<Book> getBooksByBookName(String bookName) {
        return bookRepository.findBooksByBookName(bookName);
    }

    public List<Book> getBooksByPublicationYear(Integer year) {
        return bookRepository.findBooksByPublicationYear(year);
    }

    @Transactional
    public void updateBookById(BookDto bookDto) {
        Book bookWithId = bookRepository.findById(bookDto.getId()).orElseThrow(() ->
                new EntityNotFoundException("Book with id: %d not exists!".formatted(bookDto.getId())));

        updateBook(bookWithId, bookDto);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBook(Book currBook, BookDto bookDto) {
        if (isCanSetBookName(currBook, bookDto.getBookName())) {
            currBook.setBookName(bookDto.getBookName());
        }

        if (isCanSetAuthorName(currBook, bookDto.getAuthorName())) {
            currBook.setAuthorName(bookDto.getAuthorName());
        }

        if (isCanSetPublicationYear(currBook, bookDto.getPublicationYear())) {
            currBook.setPublicationYear(bookDto.getPublicationYear());
        }
    }

    private boolean isCanSetBookName(Book currentBook, String newBookName) {
        return currentBook.getBookName() == null
                || newBookName != null && !currentBook.getBookName().equals(newBookName);
    }

    private boolean isCanSetAuthorName(Book currBook, String newAuthorName) {
        return currBook.getAuthorName() == null
                || newAuthorName != null && !currBook.getAuthorName().equals(newAuthorName);
    }

    private boolean isCanSetPublicationYear(Book currBook, Integer newPublicationYear) {
        return currBook.getPublicationYear() == null
                || newPublicationYear != null && !currBook.getPublicationYear().equals(newPublicationYear);
    }
}
