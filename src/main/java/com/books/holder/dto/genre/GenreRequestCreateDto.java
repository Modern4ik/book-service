package com.books.holder.dto.genre;

import com.books.holder.validation.annotations.UniqueGenre;
import jakarta.validation.constraints.NotBlank;

public record GenreRequestCreateDto(@NotBlank @UniqueGenre String name) {
}
