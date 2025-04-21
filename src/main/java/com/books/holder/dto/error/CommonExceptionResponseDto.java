package com.books.holder.dto.error;

public record CommonExceptionResponseDto(String exceptionMessage, int status, String timeStamp) {
}
