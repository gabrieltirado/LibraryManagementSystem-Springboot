package com.example.lms.repository;

import com.example.lms.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    BorrowingRecord findByUserIdAndBookId(Long userId, Long bookId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
    long countByUserIdAndReturnedDateIsNull(Long userId);
    Optional<BorrowingRecord> findByUserIdAndBookIdAndReturnedDateIsNull(Long userId, Long bookId);
    List<BorrowingRecord> findAllByUserIdOrderByBorrowDateDesc(Long userId);
    List<BorrowingRecord> findByBookIdAndReturnedDateIsNull(Long bookId);
    List<BorrowingRecord> findByUserIdAndReturnedDateIsNull(Long userId);

    @Query("SELECT br FROM BorrowingRecord br WHERE br.returnedDate IS NULL AND br.dueDate <= :today")
    List<BorrowingRecord> findActiveOverdueRecords(Date today);
}
