package com.books.holder.specifications;

import com.books.holder.dto.author.AuthorRequestDto;
import com.books.holder.entity.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class AuthorSpecification {

    public Specification<Author> generateAuthorSpec(AuthorRequestDto authorRequestDto) {
        Specification<Author> spec = Specification.where(null);

        if (authorRequestDto.firstName() != null && !authorRequestDto.firstName().isEmpty()) {
            spec = spec.and(hasFirstName(authorRequestDto.firstName()));
        }

        if (authorRequestDto.lastName() != null && !authorRequestDto.lastName().isEmpty()) {
            spec = spec.and(hasLastName(authorRequestDto.lastName()));
        }

        if (authorRequestDto.birthday() != null) {
            spec = spec.and(hasBirthday(authorRequestDto.birthday()));
        }

        if (authorRequestDto.country() != null) {
            spec = spec.and(hasCountry(authorRequestDto.country()));
        }

        return spec;
    }


    private Specification<Author> hasFirstName(String firstName) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")),
                        firstName.toLowerCase());
    }

    private Specification<Author> hasLastName(String lastName) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")),
                        lastName.toLowerCase());
    }

    private Specification<Author> hasBirthday(Date birthday) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("birthday"), birthday);
    }

    private Specification<Author> hasCountry(String country) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")),
                        country.toLowerCase());
    }

}
