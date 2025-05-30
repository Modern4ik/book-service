package com.library.book_service.service.book;

import com.library.book_service.constants.MessageTemplates;
import com.library.book_service.dto.book.BookRequestCreateDto;
import com.library.book_service.dto.book.BookRequestFilterDto;
import com.library.book_service.dto.book.BookRequestUpdateDto;
import com.library.book_service.dto.book.BookResponseDto;
import com.library.book_service.entity.Author;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Genre;
import com.library.book_service.mappers.BookMapper;
import com.library.book_service.repository.AuthorRepository;
import com.library.book_service.repository.BookRepository;
import com.library.book_service.repository.GenreRepository;
import com.library.book_service.service.cache.CacheVersionService;
import com.library.book_service.specifications.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String CACHE_NAMESPACE = "books";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;
    private final BookSpecification bookSpecification;
    private final CacheVersionService cacheVersionService;

    @Override
    @Transactional
    public BookResponseDto saveBook(BookRequestCreateDto bookRequestCreateDto) {
        int authorId = bookRequestCreateDto.authorId();
        List<Integer> genresId = bookRequestCreateDto.genresId();

        Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));
        Book newBook = bookMapper.toEntity(bookRequestCreateDto);

        addGenresToBook(newBook, genresId);
        foundedAuthor.addBook(newBook);

        BookResponseDto newBookDto = bookMapper.toDto(bookRepository.save(newBook));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return newBookDto;
    }

    @Override
    @Cacheable(value = "bookById", key = "#id")
    public BookResponseDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.BOOK_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    @Cacheable(value = "booksByFilter", key = "{#bookRequestFilterDto, @cacheVersionService.getCurrentVersion('books')}")
    public List<BookResponseDto> getBooks(BookRequestFilterDto bookRequestFilterDto) {
        return bookMapper.mapToDto(bookRepository.findAll(
                bookSpecification.generateBookSpec(bookRequestFilterDto)
        ));
    }

    @Override
    @Transactional
    @CacheEvict(value = "bookById", key = "#id")
    public BookResponseDto updateBookById(Long id, BookRequestUpdateDto bookRequestUpdateDto) {
        Book bookWithId = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.BOOK_NOT_FOUND_MESSAGE.formatted(id)));

        BookResponseDto updatedBookDto = bookMapper.toDto(updateBook(bookWithId, bookRequestUpdateDto));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return updatedBookDto;
    }

    @Override
    @CacheEvict(value = "bookById", key = "#id")
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

    private Book updateBook(Book currBook, BookRequestUpdateDto bookRequestUpdateDto) {
        String bookName = bookRequestUpdateDto.bookName();
        Integer authorId = bookRequestUpdateDto.authorId();
        List<Integer> genresId = bookRequestUpdateDto.genresId();
        Integer publicationYear = bookRequestUpdateDto.publicationYear();

        if (isCanSetBookName(currBook, bookName)) {
            currBook.setBookName(bookName);
        }

        if (isCanSetAuthor(currBook, authorId)) {
            Author foundedAuthor = authorRepository.findById(authorId).orElseThrow(() ->
                    new EntityNotFoundException(MessageTemplates.AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)));

            foundedAuthor.addBook(currBook);
        }

        if (isCanSetGenres(currBook, genresId)) {
            for (Integer genreId : genresId) {
                Genre foundedGenre = genreRepository.findById(genreId).orElseThrow(() ->
                        new EntityNotFoundException(MessageTemplates.GENRE_BY_ID_NOT_FOUND_MESSAGE.formatted(genreId)));

                currBook.addGenre(foundedGenre);
            }
        }

        if (isCanSetPublicationYear(currBook, publicationYear)) {
            currBook.setPublicationYear(publicationYear);
        }

        return currBook;
    }

    private boolean isCanSetBookName(Book currentBook, String newBookName) {
        return currentBook.getBookName() == null
                || newBookName != null && !currentBook.getBookName().equals(newBookName);
    }

    private boolean isCanSetAuthor(Book currBook, Integer newAuthor) {
        return newAuthor != null && !currBook.getAuthor().getId().equals(newAuthor);
    }

    private boolean isCanSetGenres(Book currBook, List<Integer> newGenres) {
        if (newGenres == null || newGenres.isEmpty()) {
            return false;
        }

        for (Genre genre : currBook.getGenres()) {
            newGenres.remove(genre.getId());
        }

        return !newGenres.isEmpty();
    }

    private boolean isCanSetPublicationYear(Book currBook, Integer newPublicationYear) {
        return currBook.getPublicationYear() == null
                || newPublicationYear != null && !currBook.getPublicationYear().equals(newPublicationYear);
    }

    private void addGenresToBook(Book newBook, List<Integer> genresId) {
        for (Integer genreId : genresId) {
            Genre foundedGenre = genreRepository.findById(genreId).orElseThrow(() ->
                    new EntityNotFoundException(MessageTemplates.GENRE_BY_ID_NOT_FOUND_MESSAGE.formatted(genreId)));
            newBook.addGenre(foundedGenre);
        }
    }

}
