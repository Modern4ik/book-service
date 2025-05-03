package com.books.holder.dto.comment;

import java.time.LocalDateTime;

public record CommentRequestFilterDto(Long bookId, Long userId, LocalDateTime createdAt) {
}
