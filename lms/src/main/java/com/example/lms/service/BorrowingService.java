package com.example.lms.service;

import com.example.lms.exception.AlreadyBorrowedException;
import com.example.lms.exception.BookNotAvailableException;
import com.example.lms.exception.BookNotFoundException;
import com.example.lms.exception.BorrowingLimitExceededException;
import com.example.lms.exception.UserNotFoundException;
import com.example.lms.model.BorrowingRecord;
import com.example.lms.model.User;
import com.example.lms.model.Book;
import com.example.lms.repository.BorrowingRecordRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.isAvailability()) {
            throw new BookNotAvailableException("Book is not available for borrowing");
        }

        // Check if the user has reached the borrowing limit
        if (!canUserBorrow(user)) {
            throw new BorrowingLimitExceededException("User has reached the borrowing limit (2 books)");
        }
        if (borrowingRecordRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new AlreadyBorrowedException("User already borrowed this book");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setUser(user);
        borrowingRecord.setBook(book);
        borrowingRecord.setBorrowDate(new Date());

        // Due date is set to 14 days from the borrow date
        Date dueDate = new Date();
        dueDate.setTime(dueDate.getTime() + (14 * 24 * 60 * 60 * 1000)); // 14 days from now
        borrowingRecord.setDueDate(dueDate);
        borrowingRecord.setReturnedDate(null);

        // Update book availability when a book is borrowed or returned
        updateAvailability(book);

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public BorrowingRecord returnBook(Long userId, Long bookId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findByUserIdAndBookIdAndReturnedDateIsNull(userId, bookId)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found"));
        //return date should be set to null if the book is returned
        borrowingRecord.setReturnedDate(new Date());

        // Update book availability getting the book from the record
        Book book = borrowingRecord.getBook();
        updateAvailability(book);

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public List<BorrowingRecord> getBorrowingHistory(Long userId) {
        return borrowingRecordRepository.findAllByUserIdOrderByBorrowDateDesc(userId);
    }

    public List<Book> getCurrentlyBorrowedBooks(Long userId) {
        return borrowingRecordRepository.findByUserIdAndReturnedDateIsNull(userId)
                .stream()
                .map(BorrowingRecord::getBook)
                .collect(Collectors.toList());
    }

    public boolean canUserBorrow(User user) {
        long activeLoans = user.getBorrowingRecords().stream()
                .filter(BorrowingRecord::isActive)
                .count();

        return activeLoans < 2;
    }

    /**
     * Updates the availability of a book based on its borrowing records.
     * If there are borrowing records with ReturnedDate as null, the book is not available.
     * If there are no such records, the book is available.
     *
     * @param book The book to update.
     */
    @Transactional
    public void updateAvailability(Book book) {
        Book managedBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        boolean available = borrowingRecordRepository
                .findByBookIdAndReturnedDateIsNull(book.getId())
                .isEmpty();

        managedBook.setAvailability(available);
        bookRepository.save(managedBook);
    }

    public List<BorrowingRecord> getActiveOverdueRecords() {
        Date today = new Date();
        return borrowingRecordRepository.findActiveOverdueRecords(today);
    }




}