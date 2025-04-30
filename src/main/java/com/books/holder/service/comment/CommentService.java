package com.books.holder.service.comment;

import com.books.holder.dto.comment.CommentRequestCreateDto;
import com.books.holder.dto.comment.CommentRequestFilterDto;
import com.books.holder.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {

    String CACHE_NAMESPACE = "comments";

    CommentResponseDto saveComment(CommentRequestCreateDto commentRequestCreateDto);

    CommentResponseDto getCommentById(Long id);

    List<CommentResponseDto> getComments(CommentRequestFilterDto commentRequestFilterDto);

    void deleteCommentById(Long id);

}
