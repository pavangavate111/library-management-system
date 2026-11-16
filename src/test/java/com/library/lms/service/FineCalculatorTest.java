
package com.library.lms.service;

import com.library.lms.entity.Borrowing;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FineCalculatorTest {

    private final FineCalculator fineCalculator =
            new FineCalculator();

    @Test
    void shouldCalculateFineForLateReturn() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.of(2026, 11, 10));
        borrowing.setReturnDate(LocalDate.of(2026, 11, 13));

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(new BigDecimal("15"), fine);
    }

    @Test
    void shouldReturnZeroFineForOnTimeReturn() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.of(2026, 11, 10));
        borrowing.setReturnDate(LocalDate.of(2026, 11, 10));

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }

    @Test
    void shouldReturnZeroFineForEarlyReturn() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.of(2026, 11, 10));
        borrowing.setReturnDate(LocalDate.of(2026, 11, 8));

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }

    @Test
    void shouldCalculateFineForUnreturnedOverdueBook() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now().minusDays(3));
        borrowing.setReturnDate(null);

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(new BigDecimal("15"), fine);
    }

    @Test
    void shouldReturnZeroFineForUnreturnedBookNotOverdue() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now().plusDays(5));
        borrowing.setReturnDate(null);

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }



    @Test
    void shouldCalculateFineForReturnedOverdueBook() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now().minusDays(3));
        borrowing.setReturnDate(LocalDate.now());

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.valueOf(15), fine);
    }

    @Test
    void shouldReturnZeroFineWhenBookIsReturnedOnDueDate() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now());
        borrowing.setReturnDate(LocalDate.now());

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }

    @Test
    void shouldCalculateCorrectFineForMultipleOverdueDays() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now().minusDays(10));
        borrowing.setReturnDate(LocalDate.now());

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.valueOf(50), fine);
    }

    @Test
    void shouldReturnZeroFineWhenBookIsReturnedEarly() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now().plusDays(5));
        borrowing.setReturnDate(LocalDate.now());

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }

    @Test
    void shouldReturnZeroFineWhenUnreturnedBookIsDueToday() {

        Borrowing borrowing = new Borrowing();

        borrowing.setDueDate(LocalDate.now());
        borrowing.setReturnDate(null);

        BigDecimal fine = fineCalculator.calculate(borrowing);

        assertEquals(BigDecimal.ZERO, fine);
    }
    
}
