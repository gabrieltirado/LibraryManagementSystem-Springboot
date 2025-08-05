package com.example.lms.service;

import com.example.lms.exception.BookNotFoundException;
import com.example.lms.model.Book;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Searches for books by title, author, or ISBN.
     *
     * @param query the search query
     * @return a list of books matching the search criteria
     */
    @Cacheable("books")
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCaseOrIsbnContainingIgnoreCase(
                query, query, query
        );
    }

}