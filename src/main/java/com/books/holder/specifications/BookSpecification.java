package com.books.holder.specifications;

import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.entity.Author;
import com.books.holder.entity.Book;
import com.books.holder.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecification {

    private static final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";

    private final AuthorRepository authorRepository;

    public Specification<Book> generateBookSpec(BookRequestFilterDto bookRequestFilterDto) {
        Specification<Book> spec = Specification.where(null);

        String bookName = bookRequestFilterDto.bookName();
        Integer authorId = bookRequestFilterDto.authorId();
        Integer publicationYear = bookRequestFilterDto.publicationYear();

        if (bookName != null && !bookName.isEmpty()) {
            spec = spec.and(hasBookName(bookName));
        }

        if (authorId != null) {
            spec = spec.and(hasAuthor(
                    authorRepository.findById(authorId).orElseThrow(() ->
                            new EntityNotFoundException(
                                    AUTHOR_NOT_FOUND_MESSAGE.formatted(authorId)))));
        }

        if (publicationYear != null) {
            spec = spec.and(hasPublicationYear(publicationYear));
        }

        return spec;
    }

    private Specification<Book> hasBookName(String bookName) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("bookName")),
                        bookName.toLowerCase());
    }

    private Specification<Book> hasAuthor(Author author) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author"), author);
    }

    private Specification<Book> hasPublicationYear(Integer publicationYear) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publicationYear"), publicationYear);
    }
}
