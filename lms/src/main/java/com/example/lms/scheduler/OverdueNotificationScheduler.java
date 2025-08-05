package com.example.lms.scheduler;

import com.example.lms.model.BorrowingRecord;
import com.example.lms.service.BorrowingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OverdueNotificationScheduler {

    @Autowired
    private BorrowingService borrowingService;


    // Runs every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendOverdueNotifications() {
        List<BorrowingRecord> overdueRecords = borrowingService.getActiveOverdueRecords();
        overdueRecords.forEach(record -> {
            log.info("Sending notification for overdue book: {} to user: {}",
                    record.getBook().getTitle(), record.getUser().getId());
        });
    }
}