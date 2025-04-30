package com.books.holder.mappers;

import com.books.holder.dto.comment.CommentRequestCreateDto;
import com.books.holder.dto.comment.CommentResponseDto;
import com.books.holder.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "userId", source = "user.id")
    CommentResponseDto toDto(Comment comment);

    @Mapping(target = "postDate", expression = "java(LocalDateTime.now())")
    Comment toEntity(CommentRequestCreateDto commentRequestCreateDto);

    List<CommentResponseDto> mapToDto(List<Comment> comments);

}
