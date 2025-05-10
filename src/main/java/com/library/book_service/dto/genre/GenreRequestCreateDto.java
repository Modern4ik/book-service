package com.library.book_service.dto.genre;

import com.library.book_service.validation.annotations.UniqueGenre;
import jakarta.validation.constraints.NotBlank;

public record GenreRequestCreateDto(@NotBlank @UniqueGenre String name) {
}
