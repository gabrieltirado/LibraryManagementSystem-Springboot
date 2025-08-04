package com.example.lms.controller;

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
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingRecord> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        BorrowingRecord borrowingRecord = borrowingService.borrowBook(userId, bookId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.CREATED);
    }

    @Operation(summary = "Return a book", description = "Allows a user to return a borrowed book by providing user ID and book ID")
    @PostMapping("/return")
    public ResponseEntity<BorrowingRecord> returnBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return new ResponseEntity<>(borrowingService.returnBook(userId, bookId), HttpStatus.OK);
    }

    @Operation(summary = "Get borrowing history", description = "Retrieves the borrowing history of a user by user ID")
    @GetMapping("/history")
    public ResponseEntity<List<BorrowingRecord>> getHistory(@RequestParam Long userId) {
        return new ResponseEntity<>(borrowingService.getBorrowingHistory(userId), HttpStatus.OK);
    }
}