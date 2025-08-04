package com.example.lms.service;

import com.example.lms.model.BorrowingRecord;
import com.example.lms.model.User;
import com.example.lms.model.Book;
import com.example.lms.repository.BorrowingRecordRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public BorrowingRecord borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailability()) {
            throw new RuntimeException("Book is not available for borrowing");
        }

        long activeBorrows = borrowingRecordRepository.countByUserIdAndReturnedDateIsNull(userId);
        if (activeBorrows >= 2) {
            throw new RuntimeException("User has reached the borrowing limit (2 books)");
        }
        if (borrowingRecordRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("User already borrowed this book");
        }

        BorrowingRecord record = new BorrowingRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(new Date());

        // Due date is set to 14 days from the borrow date
        Date dueDate = new Date();
        dueDate.setTime(dueDate.getTime() + (14 * 24 * 60 * 60 * 1000)); // 14 days from now
        record.setDueDate(dueDate);
        record.setReturnedDate(null);

        book.setAvailability(false);
        bookRepository.save(book);

        return borrowingRecordRepository.save(record);
    }

    public BorrowingRecord returnBook(Long userId, Long bookId) {
        BorrowingRecord record = borrowingRecordRepository
                .findByUserIdAndBookIdAndReturnedDateIsNull(userId, bookId)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found"));
        record.setReturnedDate(new Date());

        // Update book availability getting the book from the record
        Book book = record.getBook();
        book.setAvailability(true);
        bookRepository.save(book);

        return borrowingRecordRepository.save(record);
    }

    public List<BorrowingRecord> getBorrowingHistory(Long userId) {
        return borrowingRecordRepository.findAllByUserIdOrderByBorrowDateDesc(userId);
    }
}