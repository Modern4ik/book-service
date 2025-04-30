package com.books.holder.dto.user;

import java.time.LocalDate;

public record UserResponseDto(Long id,
                              String nickname,
                              String firstName,
                              String lastName,
                              String email,
                              LocalDate registrationDate) {
}
