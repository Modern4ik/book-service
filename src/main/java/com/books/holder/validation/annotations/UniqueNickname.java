package com.books.holder.validation.annotations;

import com.books.holder.validation.validators.UniqueNicknameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNicknameValidator.class)
public @interface UniqueNickname {

    String message() default "Username is already taken!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
