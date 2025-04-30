package com.books.holder.service.comment;

import com.books.holder.constants.MessageTemplates;
import com.books.holder.dto.comment.CommentRequestCreateDto;
import com.books.holder.dto.comment.CommentRequestFilterDto;
import com.books.holder.dto.comment.CommentResponseDto;
import com.books.holder.entity.Book;
import com.books.holder.entity.Comment;
import com.books.holder.entity.User;
import com.books.holder.mappers.CommentMapper;
import com.books.holder.repository.BookRepository;
import com.books.holder.repository.CommentRepository;
import com.books.holder.repository.UserRepository;
import com.books.holder.service.cache.CacheVersionService;
import com.books.holder.specifications.CommentSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final CommentSpecification commentSpecification;
    private final CommentMapper commentMapper;
    private final CacheVersionService cacheVersionService;

    @Override
    @Transactional
    public CommentResponseDto saveComment(CommentRequestCreateDto commentRequestCreateDto) {
        Long bookId = commentRequestCreateDto.bookId();
        Long userId = commentRequestCreateDto.userId();

        Book bookForComment = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        User userForComment = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.USER_NOT_FOUND_MESSAGE.formatted(userId)));

        Comment newComment = commentMapper.toEntity(commentRequestCreateDto);
        newComment.setBook(bookForComment);
        newComment.setUser(userForComment);

        CommentResponseDto newCommentDto = commentMapper.toDto(commentRepository.save(newComment));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return newCommentDto;
    }

    @Override
    @Cacheable(value = "commentById", key = "#id")
    public CommentResponseDto getCommentById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.COMMENT_NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    @Cacheable(value = "commentsByFilter", key = "{#commentRequestFilterDto, @cacheVersionService.getCurrentVersion('comments')}")
    public List<CommentResponseDto> getComments(CommentRequestFilterDto commentRequestFilterDto) {
        return commentMapper.mapToDto(
                commentRepository.findAll(commentSpecification.generateCommentSpec(commentRequestFilterDto)));
    }

    @Override
    @Transactional
    @CacheEvict(value = "commentById", key = "#id")
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

}
