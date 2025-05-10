package com.library.book_service.mappers;

import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    CommentResponseDto toDto(Comment comment);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Comment toEntity(CommentRequestCreateDto commentRequestCreateDto);

    List<CommentResponseDto> mapToDto(List<Comment> comments);

}
