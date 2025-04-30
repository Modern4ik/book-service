package com.books.holder.dto.user;

import java.time.LocalDate;

public record UserRequestFilterDto(String firstName, String lastName, LocalDate registrationDate) {
}
