package com.books.holder.advice;

import com.books.holder.dto.error.CommonExceptionResponseDto;
import com.books.holder.dto.error.ValidationExceptionResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonExceptionResponseDto handleCommonException(Exception ex) {
        return new CommonExceptionResponseDto(
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now().toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponseDto handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        return new ValidationExceptionResponseDto(
                "Validation failed!", HttpStatus.BAD_REQUEST.value(), Instant.now().toString(), errors);
    }

}
