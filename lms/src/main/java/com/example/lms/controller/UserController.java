package com.example.lms.controller;

import com.example.lms.model.User;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // According to the Single Responsibility Principle (S from SOLID), these endpoints
    // should not be in this controller, but are included here to match the required API paths:
    // GET /api/users/{userId}/books - View borrowed books
    // GET /api/users/{userId}/history - View borrowing history

    // These methods are commented out to avoid cluttering the UserController with borrowing-related logic.
    // They should ideally be in a separate BorrowingController

    /*
    @GetMapping("/{userId}/books")
    public List<Book> getBorrowedBooks(@PathVariable Long userId) {
        return userService.getCurrentlyBorrowedBooks(userId);
    }

    @GetMapping("/{userId}/history")
    public List<BorrowingRecord> getBorrowingHistory(@PathVariable Long userId) {
        return userService.getBorrowingHistory(userId);
    }
    */
}