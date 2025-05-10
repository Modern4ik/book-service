package com.library.book_service.validation.annotations;

import com.library.book_service.validation.validators.UniqueGenreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueGenreValidator.class)
public @interface UniqueGenre {

    String message() default "Genre is already exist!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
