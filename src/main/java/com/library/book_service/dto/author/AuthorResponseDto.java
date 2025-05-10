package com.library.book_service.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String country;

}
