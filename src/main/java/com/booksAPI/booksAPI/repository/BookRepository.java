package com.booksAPI.booksAPI.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<LibBook, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "CREATE TABLE IF NOT EXISTS lib_books(" +
            "id BIGSERIAL PRIMARY KEY, " +
            "book_name VARCHAR(100) NOT NULL, " +
            "author_name VARCHAR(100), " +
            "publication_year INTEGER" +
            ")", nativeQuery = true)
    void createBooksTable();

    @Query(value = "SELECT * FROM lib_books WHERE LOWER(author_name) = :authorName", nativeQuery = true)
    List<LibBook> findAllBooksByAuthorName(String authorName);

    @Query(value = "SELECT * FROM lib_books WHERE LOWER(book_name) = :bookName", nativeQuery = true)
    List<LibBook> findAllBooksByBookName(String bookName);

    @Query(value = "SELECT * FROM lib_books WHERE publication_year = :year", nativeQuery = true)
    List<LibBook> findAllBooksByPublicationYear(Integer year);
}
