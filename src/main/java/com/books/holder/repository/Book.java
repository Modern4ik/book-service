package com.books.holder.repository;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @Column(name = "publication_year")
    private Integer publicationYear;
}
