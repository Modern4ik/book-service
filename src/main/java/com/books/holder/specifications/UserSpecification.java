package com.books.holder.specifications;

import com.books.holder.dto.user.UserRequestFilterDto;
import com.books.holder.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserSpecification {

    public Specification<User> generateUserSpecification(UserRequestFilterDto filterDto) {
        Specification<User> spec = Specification.where(null);

        String firstName = filterDto.firstName();
        String lastName = filterDto.lastName();
        LocalDate registrationDate = filterDto.registrationDate();

        if (firstName != null && !firstName.isEmpty()) {
            spec = spec.and(hasFirstName(firstName));
        }

        if (lastName != null && !lastName.isEmpty()) {
            spec = spec.and(hasLastName(lastName));
        }

        if (registrationDate != null) {
            spec = spec.and(hasRegistrationDate(registrationDate));
        }

        return spec;
    }

    private Specification<User> hasFirstName(String firstName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(builder.lower(root.get("firstName")), firstName.toLowerCase());
    }

    private Specification<User> hasLastName(String lastName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(builder.lower(root.get("lastName")), lastName.toLowerCase());
    }

    private Specification<User> hasRegistrationDate(LocalDate registrationDate) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(root.get("registrationDate"), registrationDate);
    }

}
