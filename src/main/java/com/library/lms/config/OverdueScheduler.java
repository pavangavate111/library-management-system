package com.library.lms.config;

import com.library.lms.service.BorrowingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueScheduler {

    private final BorrowingService borrowingService;

    public OverdueScheduler(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateOverdueBorrowings() {
        borrowingService.updateOverdueBorrowings();
    }
}