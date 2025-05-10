package com.library.book_service.validation.validators;

import com.library.book_service.validation.annotations.NotOne;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotOneValidator implements ConstraintValidator<NotOne, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value > 1;
    }

}
