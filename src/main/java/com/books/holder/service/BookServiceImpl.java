package com.books.holder.service;

import com.books.holder.repository.BookRepository;
import com.books.holder.repository.Book;
import com.books.holder.dto.BookDto;
import com.books.holder.mappers.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public void saveNewBook(BookDto bookDto) {
        bookRepository.save(bookMapper.toEntity(bookDto));
    }

    public List<BookDto> getAllBooks() {
        return bookMapper.mapToDto(bookRepository.findAll());
    }

    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id: %d not exists!".formatted(id))));
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        return bookMapper.mapToDto(bookRepository.findBooksByAuthorName(authorName));
    }

    public List<BookDto> getBooksByBookName(String bookName) {
        return bookMapper.mapToDto(bookRepository.findBooksByBookName(bookName));
    }

    public List<BookDto> getBooksByPublicationYear(Integer year) {
        return bookMapper.mapToDto(bookRepository.findBooksByPublicationYear(year));
    }

    @Transactional
    public void updateBookById(BookDto bookDto) {
        Book bookWithId = bookRepository.findById(bookDto.id()).orElseThrow(() ->
                new EntityNotFoundException("Book with id: %d not exists!".formatted(bookDto.id())));

        updateBook(bookWithId, bookDto);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBook(Book currBook, BookDto bookDto) {
        if (isCanSetBookName(currBook, bookDto.bookName())) {
            currBook.setBookName(bookDto.bookName());
        }

        if (isCanSetAuthorName(currBook, bookDto.authorName())) {
            currBook.setAuthorName(bookDto.authorName());
        }

        if (isCanSetPublicationYear(currBook, bookDto.publicationYear())) {
            currBook.setPublicationYear(bookDto.publicationYear());
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
