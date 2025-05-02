package com.books.holder.dto.user;

import java.time.LocalDateTime;

public record UserRequestFilterDto(String firstName, String lastName, LocalDateTime createdAt) {
}
