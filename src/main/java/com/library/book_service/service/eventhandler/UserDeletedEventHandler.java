package com.library.book_service.service.eventhandler;

import com.library.common.events.UserDeletedEvent;
import com.library.book_service.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeletedEventHandler {

    private final CommentService commentService;

    @KafkaListener(topics = "user-deleted-topic")
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        commentService.deleteCommentsByUserId(event.userId());
    }

}
