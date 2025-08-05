package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowingRecord;
import com.example.lms.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@Tag(name = "Borrowing", description = "Borrowing management APIs")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book by providing user ID and book ID")
    @PostMapping("/users/{userId}/borrow/{bookId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        BorrowingRecord borrowingRecord = borrowingService.borrowBook(userId, bookId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.CREATED);
    }

    @Operation(summary = "Return a book", description = "Allows a user to return a borrowed book by providing user ID and book ID")
    @PutMapping("/users/{userId}/return/{bookId}")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return new ResponseEntity<>(borrowingService.returnBook(userId, bookId), HttpStatus.OK);
    }

    @Operation(summary = "Get borrowing history", description = "Retrieves the borrowing history of a user by user ID")
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<BorrowingRecord>> getBorrowingHistory(@PathVariable Long userId) {
        return new ResponseEntity<>(borrowingService.getBorrowingHistory(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/books")
    public ResponseEntity<List<Book>> getBorrowedBooks(@PathVariable Long userId) {
        return new ResponseEntity<>(borrowingService.getCurrentlyBorrowedBooks(userId), HttpStatus.OK);
    }


}