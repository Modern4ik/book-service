package com.books.holder.validation.validators;

import com.books.holder.service.genre.GenreService;
import com.books.holder.validation.annotations.UniqueGenre;
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
