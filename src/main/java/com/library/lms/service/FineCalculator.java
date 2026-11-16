package com.library.lms.service;

import com.library.lms.entity.Borrowing;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class FineCalculator {

    private static final BigDecimal DAILY_FINE_AMOUNT =
            BigDecimal.valueOf(5);

    public BigDecimal calculate(Borrowing borrowing) {

        LocalDate effectiveDate = borrowing.getReturnDate() != null
                ? borrowing.getReturnDate()
                : LocalDate.now();

        long overdueDays = ChronoUnit.DAYS.between(
                borrowing.getDueDate(),
                effectiveDate
        );

        if (overdueDays <= 0) {
            return BigDecimal.ZERO;
        }

        return DAILY_FINE_AMOUNT.multiply(
                BigDecimal.valueOf(overdueDays)
        );
    }


}