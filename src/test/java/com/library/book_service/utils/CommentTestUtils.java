package com.library.book_service.utils;

import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentTestUtils {

    public static CommentRequestCreateDto generateCommentCreateDto(String content, long bookId, long userId) {
        return new CommentRequestCreateDto(content, bookId, userId);
    }

    public static CommentRequestFilterDto generateFilterDto(long bookId, long userId, LocalDateTime createdAt) {
        return new CommentRequestFilterDto(bookId, userId, createdAt);
    }

    public static CommentResponseDto generateResponseDto(Long id, String content, long bookId, long userId, LocalDateTime createdAt) {
        return new CommentResponseDto(id, content, bookId, userId, createdAt);
    }

    public static Comment generateComment(long id, String content, long bookId, long userId, LocalDateTime createdAt) {
        return new Comment(id,
                content,
                BookTestUtils.generateBook(bookId, "Test book", 1, null, null),
                userId,
                createdAt);
    }

    public static List<Comment> generateCommentsList(CommentRequestFilterDto filterDto, int count) {
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            comments.add(generateComment(
                    (long) i + 1,
                    "Test comment",
                    filterDto.bookId(),
                    filterDto.userId(),
                    filterDto.createdAt()
            ));
        }

        return comments;
    }

    public static List<CommentResponseDto> generateCommentsDtoList(CommentRequestFilterDto filterDto, int count) {
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            commentResponseDtos.add(generateResponseDto(
                    (long) i + 1,
                    "Test comment",
                    filterDto.bookId(),
                    filterDto.userId(),
                    filterDto.createdAt()
            ));
        }

        return commentResponseDtos;
    }

}
