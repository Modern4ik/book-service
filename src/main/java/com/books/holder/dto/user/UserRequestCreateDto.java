package com.books.holder.dto.user;

import com.books.holder.validation.annotations.UniqueEmail;
import com.books.holder.validation.annotations.UniqueNickname;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequestCreateDto(@NotBlank @UniqueNickname String nickname,
                                   String firstName,
                                   String lastName,
                                   @NotNull @Email @UniqueEmail String email,
                                   @NotNull LocalDate registrationDate) {
}
