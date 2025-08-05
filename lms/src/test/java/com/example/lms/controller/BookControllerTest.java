package com.example.lms.controller;

import com.example.lms.model.Author;
import com.example.lms.model.Book;
import com.example.lms.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void addBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor(Author.builder().name("Author").build());

        when(bookService.addBook(any(Book.class))).thenReturn(book);

        Book result = bookController.addBook(book);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        assertEquals("Author", result.getAuthor().getName());
        verify(bookService, times(1)).addBook(book);
    }
}