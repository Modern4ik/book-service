package com.library.book_service.specifications;

import com.library.book_service.dto.author.AuthorRequestFilterDto;
import com.library.book_service.entity.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuthorSpecification {

    public Specification<Author> generateAuthorSpec(AuthorRequestFilterDto authorRequestFilterDto) {
        Specification<Author> spec = Specification.where(null);

        if (authorRequestFilterDto.firstName() != null && !authorRequestFilterDto.firstName().isEmpty()) {
            spec = spec.and(hasFirstName(authorRequestFilterDto.firstName()));
        }

        if (authorRequestFilterDto.lastName() != null && !authorRequestFilterDto.lastName().isEmpty()) {
            spec = spec.and(hasLastName(authorRequestFilterDto.lastName()));
        }

        if (authorRequestFilterDto.birthday() != null) {
            spec = spec.and(hasBirthday(authorRequestFilterDto.birthday()));
        }

        if (authorRequestFilterDto.country() != null) {
            spec = spec.and(hasCountry(authorRequestFilterDto.country()));
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

    private Specification<Author> hasBirthday(LocalDate birthday) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("birthday"), birthday);
    }

    private Specification<Author> hasCountry(String country) {
        return (Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")),
                        country.toLowerCase());
    }

}
