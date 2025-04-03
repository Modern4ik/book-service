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

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MappingUtils mappingUtils;

    public void saveNewBook(BookDto bookDto) {
        bookRepository.save(mappingUtils.mapToBookEntity(bookDto));
    }

    public List<BookDto> getAllBooks() {
        return mappingUtils.mapToListOfBookDto(bookRepository.findAll());
    }

    public BookDto getBookById(Long id) {
        return mappingUtils.mapToBookDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id: %d not exists!".formatted(id))));
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        return mappingUtils.mapToListOfBookDto(bookRepository.findBooksByAuthorName(authorName));
    }

    public List<BookDto> getBooksByBookName(String bookName) {
        return mappingUtils.mapToListOfBookDto(bookRepository.findBooksByBookName(bookName));
    }

    public List<BookDto> getBooksByPublicationYear(Integer year) {
        return mappingUtils.mapToListOfBookDto(bookRepository.findBooksByPublicationYear(year));
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
