package com.books.holder.specifications;

import com.books.holder.repository.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class AuthorSpecification {

    public Specification<Author> hasFirstName(String firstName) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")),
                        firstName.toLowerCase());
    }

    public Specification<Author> hasLastName(String lastName) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")),
                        lastName.toLowerCase());
    }

    public Specification<Author> hasBirthday(Date birthday) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("birthday"), birthday);
    }

    public Specification<Author> hasCountry(String country) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")),
                        country.toLowerCase());
    }

}
