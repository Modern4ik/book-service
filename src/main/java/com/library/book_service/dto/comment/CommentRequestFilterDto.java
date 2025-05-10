package com.library.book_service.dto.comment;

import java.time.LocalDateTime;

public record CommentRequestFilterDto(Long bookId, Long userId, LocalDateTime createdAt) {
}
