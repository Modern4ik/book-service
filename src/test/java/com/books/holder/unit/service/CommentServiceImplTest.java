package com.books.holder.unit.service;

import com.books.holder.dto.comment.CommentRequestCreateDto;
import com.books.holder.dto.comment.CommentRequestFilterDto;
import com.books.holder.dto.comment.CommentResponseDto;
import com.books.holder.entity.Book;
import com.books.holder.entity.Comment;
import com.books.holder.entity.User;
import com.books.holder.mappers.CommentMapperImpl;
import com.books.holder.repository.BookRepository;
import com.books.holder.repository.CommentRepository;
import com.books.holder.repository.UserRepository;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.service.comment.CommentServiceImpl;
import com.books.holder.specifications.CommentSpecification;
import com.books.holder.utils.BookTestUtils;
import com.books.holder.utils.CommentTestUtils;
import com.books.holder.utils.UserTestUtils;
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
    private UserRepository userRepository;
    @Mock
    private CommentSpecification commentSpecification;
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
        User expectedUser =
                UserTestUtils.generateUser(createDto.userId(), null, null, null, null, null);

        Mockito.when(commentMapper.toEntity(createDto))
                .thenReturn(expectedNewComment);
        Mockito.when(bookRepository.findById(createDto.bookId()))
                .thenReturn(Optional.of(expectedBook));
        Mockito.when(userRepository.findById(createDto.userId()))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(commentRepository.save(expectedNewComment))
                .thenReturn(expectedNewComment);

        CommentResponseDto responseDto = commentService.saveComment(createDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(1L, responseDto.id());
        Assertions.assertEquals(createDto.content(), responseDto.content());
        Assertions.assertEquals(createDto.bookId(), responseDto.bookId());
        Assertions.assertEquals(createDto.userId(), responseDto.userId());
        Assertions.assertEquals(expectedNewComment.getPostDate(), responseDto.postDate());
    }

    @Test
    public void shouldReturnCommentById() {
        Comment expectedComment =
                CommentTestUtils.generateComment(1L, "Test comment", 1, 1, LocalDateTime.now());

        Mockito.when(commentRepository.findById(expectedComment.getId()))
                .thenReturn(Optional.of(expectedComment));

        CommentResponseDto responseDto = commentService.getCommentById(1L);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedComment.getId(), responseDto.id());
        Assertions.assertEquals(expectedComment.getContent(), responseDto.content());
    }

    @Test
    public void shouldReturnCommentsByBookIdAndUserId() {
        CommentRequestFilterDto filterDto =
                CommentTestUtils.generateFilterDto(2, 2, LocalDateTime.now());

        Mockito.when(commentRepository.findAll(commentSpecification.generateCommentSpec(filterDto)))
                .thenReturn(CommentTestUtils.generateCommentsList(filterDto, 3));

        List<CommentResponseDto> responseDtoList = commentService.getComments(filterDto);
        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(3, responseDtoList.size());
        for (CommentResponseDto responseDto : responseDtoList) {
            Assertions.assertEquals(filterDto.bookId(), responseDto.bookId());
            Assertions.assertEquals(filterDto.userId(), responseDto.userId());
        }
    }

    @Test
    public void shouldDeleteCommentById() {
        commentService.deleteCommentById(1L);

        Mockito.verify(commentRepository).deleteById(1L);
    }
}
