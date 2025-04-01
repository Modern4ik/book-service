package com.booksAPI.booksAPI.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
@Entity
@Table(name = "lib_books")
public class LibBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "publication_year")
    private Year publicationYear;

    public LibBook() {
    }

    public LibBook(Long id, String bookName, String authorName, Year publicationYear) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
    }
}
