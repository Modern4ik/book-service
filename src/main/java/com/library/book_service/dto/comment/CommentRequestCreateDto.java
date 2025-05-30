package com.library.book_service.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequestCreateDto(@NotBlank String content,
                                      @NotNull Long bookId,
                                      @NotNull Long userId) {
}
