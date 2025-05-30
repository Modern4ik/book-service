package com.library.book_service.repository;

import com.library.book_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findBooksByPublicationYear(Integer year);

    @Query(value = "SELECT * FROM books WHERE LOWER(author_name) = LOWER(:authorName)", nativeQuery = true)
    List<Book> findBooksByAuthorName(String authorName);

    @Query(value = "SELECT * FROM books WHERE LOWER(book_name) = LOWER(:bookName)", nativeQuery = true)
    List<Book> findBooksByBookName(String bookName);

}
