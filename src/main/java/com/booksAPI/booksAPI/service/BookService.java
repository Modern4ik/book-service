package com.booksAPI.booksAPI.service;

import com.booksAPI.booksAPI.repository.BookRepository;
import com.booksAPI.booksAPI.repository.LibBook;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveNewBook(LibBook newBook) {
        bookRepository.save(newBook);
    }

    public List<LibBook> readAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<LibBook> readBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<LibBook> readBooksByAuthorName(String authorName) {
        return bookRepository.findAllBooksByAuthorName(authorName.toLowerCase());
    }

    public List<LibBook> readBooksByBookName(String bookName) {
        return bookRepository.findAllBooksByBookName(bookName.toLowerCase());
    }

    public List<LibBook> readBooksByPublicationYear(Integer year) {
        return bookRepository.findAllBooksByPublicationYear(year);
    }

    @Transactional
    public void updateBookById(Long id, String authorName, Integer publicationYear) {
        Optional<LibBook> bookWithId = bookRepository.findById(id);
        if (bookWithId.isEmpty()) {
            throw new IllegalStateException("Book with id: %d not exists!".formatted(id));
        }

        LibBook foundedBook = bookWithId.get();

        if (isCanSetAuthorName(foundedBook, authorName)) {
            foundedBook.setAuthorName(authorName);
        }

        if (isCanSetPublicationDate(foundedBook, publicationYear)) {
            foundedBook.setPublicationYear(Year.of(publicationYear));
        }
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private boolean isCanSetAuthorName(LibBook currBook, String newAuthorName) {
        return currBook.getAuthorName() == null
                || newAuthorName != null && !currBook.getAuthorName().equals(newAuthorName);
    }

    private boolean isCanSetPublicationDate(LibBook currBook, Integer newPublicationYear) {
        return currBook.getPublicationYear() == null
                || newPublicationYear != null && currBook.getPublicationYear().getValue() != newPublicationYear;
    }
}
