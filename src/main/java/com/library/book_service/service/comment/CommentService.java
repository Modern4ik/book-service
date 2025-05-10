package com.library.book_service.service.comment;

import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(CommentRequestCreateDto commentRequestCreateDto);

    CommentResponseDto getCommentById(Long id);

    List<CommentResponseDto> getComments(CommentRequestFilterDto commentRequestFilterDto);

    void deleteCommentById(Long id);

    void deleteCommentsByUserId(Long userId);

}
