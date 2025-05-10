package com.library.book_service.repository;

import com.library.book_service.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findByNameIgnoreCase(String name);

    boolean existsByName(String name);

}
