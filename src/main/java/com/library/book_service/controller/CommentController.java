package com.library.book_service.controller;

import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.service.comment.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monolith/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto saveComment(@RequestBody @Valid CommentRequestCreateDto commentRequestCreateDto) {
        return commentService.saveComment(commentRequestCreateDto);
    }

    @GetMapping(path = "/{id}")
    public CommentResponseDto getCommentById(@PathVariable @NotNull Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping
    public List<CommentResponseDto> getComments(@RequestBody CommentRequestFilterDto commentRequestFilterDto) {
        return commentService.getComments(commentRequestFilterDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable @NotNull Long id) {
        commentService.deleteCommentById(id);
    }

}
