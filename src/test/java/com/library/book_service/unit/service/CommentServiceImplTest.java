package com.library.book_service.unit.service;

import com.library.book_service.client.UserServiceClient;
import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Comment;
import com.library.book_service.mappers.CommentMapperImpl;
import com.library.book_service.repository.BookRepository;
import com.library.book_service.repository.CommentRepository;
import com.library.book_service.service.cache.CacheVersionService;
import com.library.book_service.service.comment.CommentServiceImpl;
import com.library.book_service.utils.BookTestUtils;
import com.library.book_service.utils.CommentTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserServiceClient userServiceClient;
    @Spy
    private CacheVersionService cacheVersionService;
    @Spy
    private CommentMapperImpl commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void shouldSaveComment() {
        CommentRequestCreateDto createDto =
                CommentTestUtils.generateCommentCreateDto("Test content", 1L, 1L);
        Comment expectedNewComment =
                CommentTestUtils.generateComment(1L, createDto.content(), createDto.bookId(), createDto.userId(), LocalDateTime.now());
        Book expectedBook =
                BookTestUtils.generateBook(createDto.bookId(), null, 1, null, null);

        Mockito.when(commentMapper.toEntity(createDto))
                .thenReturn(expectedNewComment);
        Mockito.when(bookRepository.findById(createDto.bookId()))
                .thenReturn(Optional.of(expectedBook));
        Mockito.when(userServiceClient.existsById(createDto.userId()))
                .thenReturn(true);
        Mockito.when(commentRepository.save(expectedNewComment))
                .thenReturn(expectedNewComment);

        CommentResponseDto responseDto = commentService.saveComment(createDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals(createDto.content(), responseDto.getContent());
        Assertions.assertEquals(createDto.bookId(), responseDto.getBookId());
        Assertions.assertEquals(createDto.userId(), responseDto.getUserId());
        Assertions.assertEquals(expectedNewComment.getCreatedAt(), responseDto.getCreatedAt());
    }

    @Test
    public void shouldReturnCommentById() {
        Comment expectedComment =
                CommentTestUtils.generateComment(1L, "Test comment", 1, 1, LocalDateTime.now());

        Mockito.when(commentRepository.findById(expectedComment.getId()))
                .thenReturn(Optional.of(expectedComment));

        CommentResponseDto responseDto = commentService.getCommentById(1L);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedComment.getId(), responseDto.getId());
        Assertions.assertEquals(expectedComment.getContent(), responseDto.getContent());
    }

    @Test
    public void shouldReturnCommentsByBookIdAndUserId() {
        CommentRequestFilterDto filterDto =
                CommentTestUtils.generateFilterDto(2, 2, LocalDateTime.now());

        Mockito.when(commentRepository.findByFilters(filterDto.bookId(), filterDto.userId(), filterDto.createdAt()))
                .thenReturn(CommentTestUtils.generateCommentsList(filterDto, 3));

        List<CommentResponseDto> responseDtoList = commentService.getComments(filterDto);
        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(3, responseDtoList.size());
        for (CommentResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.bookId(), responseDto.getBookId());
            Assertions.assertEquals(filterDto.userId(), responseDto.getUserId());
        }
    }

    @Test
    public void shouldDeleteCommentById() {
        commentService.deleteCommentById(1L);

        Mockito.verify(commentRepository).deleteById(1L);
    }
}
