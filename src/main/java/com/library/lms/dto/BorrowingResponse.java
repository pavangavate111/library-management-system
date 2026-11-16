package com.library.lms.dto;

import com.library.lms.entity.BorrowingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BorrowingResponse {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowingStatus status;


    private BigDecimal fineAmount;


    public BorrowingResponse(
            Long id,
            Long bookId,
            String bookTitle,
            Long memberId,
            String memberName,
            LocalDate borrowDate,
            LocalDate dueDate,
            LocalDate returnDate,
            BorrowingStatus status,
            BigDecimal fineAmount) {

        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
    }
    // Generate getters for all fields.
}