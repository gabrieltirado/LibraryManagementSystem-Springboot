package com.example.lms.controller;

import com.example.lms.model.BorrowingRecord;
import com.example.lms.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public BorrowingRecord borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return borrowingService.borrowBook(userId, bookId);
    }

    @PostMapping("/return")
    public BorrowingRecord returnBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return borrowingService.returnBook(userId, bookId);
    }

    @GetMapping("/history")
    public List<BorrowingRecord> getHistory(@RequestParam Long userId) {
        return borrowingService.getBorrowingHistory(userId);
    }
}