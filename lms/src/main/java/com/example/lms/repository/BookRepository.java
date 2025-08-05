package com.example.lms.repository;

import com.example.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    Book findByIsbn(String isbn);
    List<Book> findByAuthorName(String authorName);
    List<Book> findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCaseOrIsbnContainingIgnoreCase(
            String title, String author, String isbn
    );
}