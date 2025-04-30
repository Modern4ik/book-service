package com.books.holder.dto.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(Long id,
                                 String content,
                                 Long bookId,
                                 Long userId,
                                 LocalDateTime postDate) {
}
