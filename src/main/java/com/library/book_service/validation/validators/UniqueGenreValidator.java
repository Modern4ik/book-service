package com.library.book_service.validation.validators;

import com.library.book_service.service.genre.GenreService;
import com.library.book_service.validation.annotations.UniqueGenre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueGenreValidator implements ConstraintValidator<UniqueGenre, String> {

    private final GenreService genreService;

    @Override
    public boolean isValid(String genreName, ConstraintValidatorContext constraintValidatorContext) {
        return !genreService.genreExists(genreName);
    }

}
