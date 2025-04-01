package com.booksAPI.booksAPI.service;

import com.booksAPI.booksAPI.repository.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final BookRepository bookRepository;

    public EventService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createTableAfterStartup() {
        bookRepository.createBooksTable();
    }

}
