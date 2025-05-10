package com.library.book_service.validation.annotations;


import com.library.book_service.validation.validators.NotOneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotOneValidator.class)
public @interface NotOne {

    String message() default "Should not be equals one";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
