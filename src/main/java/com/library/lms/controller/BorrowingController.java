package com.library.lms.controller;

import com.library.lms.dto.BorrowingRequest;
import com.library.lms.dto.BorrowingResponse;
import com.library.lms.entity.Borrowing;
import com.library.lms.mapper.BorrowingMapper;
import com.library.lms.service.BorrowingService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BorrowingMapper borrowingMapper;


    public BorrowingController(
            BorrowingService borrowingService,
            BorrowingMapper borrowingMapper) {

        this.borrowingService = borrowingService;
        this.borrowingMapper = borrowingMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingResponse> borrowBook(
            @Valid @RequestBody BorrowingRequest request) {

        Borrowing borrowing = borrowingService.borrowBook(
                request.getBookId(),
                request.getMemberId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowingMapper.toResponse(borrowing));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/return/{borrowingId}")
    public ResponseEntity<BorrowingResponse> returnBook(
            @PathVariable Long borrowingId) {

        Borrowing borrowing = borrowingService.returnBook(borrowingId);

        return ResponseEntity.ok(
                borrowingMapper.toResponse(borrowing)
        );
    }

    @GetMapping("/member/{memberId}/history")
    public ResponseEntity<List<BorrowingResponse>> getBorrowingHistory(
            @PathVariable Long memberId) {

        List<BorrowingResponse> history = borrowingService
                .getBorrowingHistory(memberId)
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/active")
    public ResponseEntity<List<BorrowingResponse>> getActiveBorrowings() {

        List<BorrowingResponse> borrowings = borrowingService
                .getActiveBorrowings()
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();

        return ResponseEntity.ok(borrowings);
    }

    @GetMapping("/overdue")
    public List<BorrowingResponse> getOverdueBorrowings() {

        return borrowingService.getOverdueBorrowings()
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-overdue")
    public ResponseEntity<String> updateOverdueBorrowings() {

        borrowingService.updateOverdueBorrowings();

        return ResponseEntity.ok(
                "Overdue borrowing statuses updated successfully."
        );
    }

    @GetMapping("/count/borrowed")
    public long getTotalBorrowedBooks() {
        return borrowingService.getTotalBorrowedBooks();
    }

    @GetMapping("/count/returned")
    public long getTotalReturnedBooks() {
        return borrowingService.getTotalReturnedBooks();
    }

    @GetMapping("/count/overdue")
    public long getTotalOverdueBooks() {
        return borrowingService.getTotalOverdueBooks();
    }

    @GetMapping("/count")
    public long getTotalBorrowings() {
        return borrowingService.getTotalBorrowings();
    }

    @GetMapping("/book/{bookId}/history")
    public List<BorrowingResponse> getBorrowingHistoryByBook(
            @PathVariable Long bookId) {

        return borrowingService.getBorrowingHistoryByBook(bookId)
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @GetMapping("/member/{memberId}/count")
    public long getTotalBorrowingsByMember(
            @PathVariable Long memberId) {

        return borrowingService.getTotalBorrowingsByMember(memberId);
    }


    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Long> getActiveBorrowingsByMember(
            @PathVariable Long memberId) {

        long count = borrowingService.getActiveBorrowingsByMember(memberId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/member/{memberId}/returned")
    public List<BorrowingResponse> getReturnedBorrowingsByMember(
            @PathVariable Long memberId) {

        return borrowingService.getReturnedBorrowingsByMember(memberId)
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @GetMapping("/member/{memberId}/overdue")
    public List<BorrowingResponse> getOverdueBorrowingsByMember(
            @PathVariable Long memberId) {

        return borrowingService.getOverdueBorrowingsByMember(memberId)
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<BorrowingResponse>> getBorrowingsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        List<BorrowingResponse> responses =
                borrowingService.getBorrowingsBetweenDates(startDate, endDate)
                        .stream()
                        .map(borrowingMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/date-range/count")
    public ResponseEntity<Long> getTotalBorrowingsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        long count = borrowingService.getTotalBorrowingsBetweenDates(
                startDate,
                endDate
        );

        return ResponseEntity.ok(count);
    }

    @GetMapping("/returned/date-range")
    public ResponseEntity<List<BorrowingResponse>> getReturnedBorrowingsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<BorrowingResponse> responses =
                borrowingService.getReturnedBorrowingsBetweenDates(
                                startDate, endDate
                        )
                        .stream()
                        .map(borrowingMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/returned/date-range/count")
    public ResponseEntity<Long> getTotalReturnedBorrowingsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        long count =
                borrowingService.getTotalReturnedBorrowingsBetweenDates(
                        startDate, endDate
                );

        return ResponseEntity.ok(count);
    }

    @GetMapping("/{borrowingId}/fine")
    public ResponseEntity<BigDecimal> calculateFine(
            @PathVariable Long borrowingId) {

        BigDecimal fine = borrowingService.calculateFine(borrowingId);

        return ResponseEntity.ok(fine);
    }

    @GetMapping("/member/{memberId}/total-fine")
    public ResponseEntity<BigDecimal> getTotalFineByMember(
            @PathVariable Long memberId) {

        BigDecimal totalFine =
                borrowingService.getTotalFineByMember(memberId);

        return ResponseEntity.ok(totalFine);
    }

    @GetMapping("/total-fine")
    public ResponseEntity<BigDecimal> getTotalLibraryFine() {

        BigDecimal totalFine = borrowingService.getTotalLibraryFine();

        return ResponseEntity.ok(totalFine);
    }

}