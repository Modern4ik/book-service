package com.books.holder.specifications;

import com.books.holder.repository.Author;
import com.books.holder.repository.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecification {

    public Specification<Book> hasBookName(String bookName) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("bookName")),
                        bookName.toLowerCase());
    }

    public Specification<Book> hasAuthor(Author author) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author"), author);
    }

    public Specification<Book> hasPublicationYear(Integer publicationYear) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("publicationYear"), publicationYear);
    }
}
