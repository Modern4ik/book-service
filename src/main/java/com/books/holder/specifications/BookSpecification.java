package com.books.holder.specifications;

import com.books.holder.dto.book.BookRequestFilterDto;
import com.books.holder.entity.Book;
import com.books.holder.entity.Genre;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookSpecification {

    public Specification<Book> generateBookSpec(BookRequestFilterDto bookRequestFilterDto) {
        Specification<Book> spec = Specification.where(null);

        String bookName = bookRequestFilterDto.bookName();
        Integer authorId = bookRequestFilterDto.authorId();
        List<String> genreNames = bookRequestFilterDto.genreNames();
        Integer publicationYear = bookRequestFilterDto.publicationYear();

        if (bookName != null && !bookName.isEmpty()) {
            spec = spec.and(hasBookName(bookName));
        }

        if (authorId != null) {
            spec = spec.and(hasAuthor(authorId));
        }

        if (genreNames != null && !genreNames.isEmpty()) {
            spec = spec.and(hasGenres(genreNames));
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

    private Specification<Book> hasAuthor(Integer authorId) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author").get("id"), authorId);
    }

    private Specification<Book> hasGenres(List<String> genresNames) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<Book, Genre> genreJoin = root.join("genres");

            List<String> lowerGenreNames = genresNames.stream()
                    .map(String::toLowerCase)
                    .toList();

            return criteriaBuilder.lower(genreJoin.get("name")).in(lowerGenreNames);
        };
    }

    private Specification<Book> hasPublicationYear(Integer publicationYear) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publicationYear"), publicationYear);
    }
}
