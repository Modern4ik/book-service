package com.books.holder.validation.validators;

import com.books.holder.validation.annotations.NotOne;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotOneValidator implements ConstraintValidator<NotOne, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value > 1;
    }

}
