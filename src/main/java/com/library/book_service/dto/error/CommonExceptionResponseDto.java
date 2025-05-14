package com.library.book_service.dto.error;

public record CommonExceptionResponseDto(String exceptionMessage, int status, String timeStamp) {
}
