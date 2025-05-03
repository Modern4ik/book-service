package com.books.holder.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;

}
