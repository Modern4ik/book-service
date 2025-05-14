package com.library.book_service.service.comment;

import com.library.book_service.client.UserServiceClient;
import com.library.book_service.constants.MessageTemplates;
import com.library.book_service.dto.comment.CommentRequestCreateDto;
import com.library.book_service.dto.comment.CommentRequestFilterDto;
import com.library.book_service.dto.comment.CommentResponseDto;
import com.library.book_service.entity.Book;
import com.library.book_service.entity.Comment;
import com.library.book_service.mappers.CommentMapper;
import com.library.book_service.repository.BookRepository;
import com.library.book_service.repository.CommentRepository;
import com.library.book_service.service.cache.CacheVersionService;
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

    private static final String CACHE_NAMESPACE = "comments";
//    private static final String USER_SERVICE_EXISTS_URL = "http://user-service/api/v1/users/exists/{id}";

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserServiceClient userServiceClient;
//    private final RestTemplate restTemplate;          #RestTemplate реализация
//    private final WebClient.Builder webClientBuilder; #WebClient реализация

    private final CommentMapper commentMapper;
    private final CacheVersionService cacheVersionService;

    @Override
    @Transactional
    public CommentResponseDto saveComment(CommentRequestCreateDto commentRequestCreateDto) {
        if (!userServiceClient.existsById(commentRequestCreateDto.userId())) {
            throw new EntityNotFoundException(
                    MessageTemplates.USER_NOT_FOUND_MESSAGE.formatted(commentRequestCreateDto.userId()));
        }

        // Rest Template реализация

//        boolean isUserExists =
//                Optional.ofNullable(
//                        restTemplate.getForObject(USER_SERVICE_EXISTS_URL, Boolean.class, commentRequestCreateDto.userId())).orElse(false);
//        if (!isUserExists) {
//            throw new EntityNotFoundException(
//                    MessageTemplates.USER_NOT_FOUND_MESSAGE.formatted(commentRequestCreateDto.userId()));
//        }

        // WebClient реализация

//        Mono<Boolean> isUserExists = webClientBuilder.build()
//                .get()
//                .uri(USER_SERVICE_EXISTS_URL, commentRequestCreateDto.userId())
//                .retrieve()
//                .bodyToMono(Boolean.class);
//        if (!Optional.ofNullable(isUserExists.block()).orElse(false)) {
//            throw new EntityNotFoundException(
//                    MessageTemplates.USER_NOT_FOUND_MESSAGE.formatted(commentRequestCreateDto.userId()));
//        }
        Long bookId = commentRequestCreateDto.bookId();


        Book bookForComment = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        Comment newComment = commentMapper.toEntity(commentRequestCreateDto);
        newComment.setBook(bookForComment);

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
        return commentMapper.mapToDto(commentRepository.findByFilters(
                commentRequestFilterDto.bookId(), commentRequestFilterDto.userId(), commentRequestFilterDto.createdAt()));
    }

    @Override
    @CacheEvict(value = "commentById", key = "#id")
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

    @Transactional
    public void deleteCommentsByUserId(Long userId) {
        commentRepository.deleteByUserId(userId);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

}
