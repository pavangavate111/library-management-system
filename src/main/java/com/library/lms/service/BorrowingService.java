package com.library.lms.service;

import com.library.lms.entity.Borrowing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BorrowingService {

    Borrowing borrowBook(Long bookId, Long memberId);

    Borrowing returnBook(Long borrowingId);

    List<Borrowing> getBorrowingHistory(Long memberId);

    List<Borrowing> getActiveBorrowings();

    List<Borrowing> getOverdueBorrowings();

    void updateOverdueBorrowings();

    long getTotalBorrowedBooks();

    long getTotalReturnedBooks();

    long getTotalOverdueBooks();

    long getTotalBorrowings();

    List<Borrowing> getBorrowingHistoryByBook(Long bookId);

    long getTotalBorrowingsByMember(Long memberId);


    long getActiveBorrowingsByMember(Long memberId);

    List<Borrowing> getReturnedBorrowingsByMember(Long memberId);

    List<Borrowing> getOverdueBorrowingsByMember(Long memberId);

    boolean hasActiveBorrowing(Long memberId, Long bookId);

    List<Borrowing> getBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Borrowing> getReturnedBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    );

    long getTotalReturnedBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal calculateFine(Long borrowingId);
    long getTotalBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal getTotalFineByMember(Long memberId);

    BigDecimal getTotalLibraryFine();
    
}