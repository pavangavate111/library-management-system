package com.library.lms.service.impl;

import com.library.lms.entity.*;
import com.library.lms.exception.*;
import com.library.lms.repository.*;
import com.library.lms.service.BorrowingService;

import com.library.lms.service.FineCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;


@Service
@Transactional
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;


    private static final int MAX_ACTIVE_BORROWINGS = 5;




    private final FineCalculator fineCalculator;

    public BorrowingServiceImpl(
            BorrowingRepository borrowingRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository,
            FineCalculator fineCalculator
    ) {

        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.fineCalculator=fineCalculator;
    }

    @Override
    public Borrowing borrowBook(Long bookId, Long memberId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book not found with ID: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException(
                                "Member not found with ID: " + memberId));


        if (hasActiveBorrowing(memberId, bookId)) {

            throw new DuplicateBorrowingException(
                    "Member has already borrowed this book."
            );
        }
        long activeBorrowings = getActiveBorrowingsByMember(memberId);

        if (activeBorrowings >= MAX_ACTIVE_BORROWINGS) {
            throw new BorrowingLimitExceededException(
                    "Member cannot borrow more than 5 books at a time."
            );
        }




        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(
                    "No available copies of this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setMember(member);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(14));
        borrowing.setStatus(BorrowingStatus.BORROWED);

        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }

    @Override
    public Borrowing returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() ->
                        new BorrowingNotFoundException(
                                "Borrowing record not found: " + borrowingId));


        if (borrowing.getStatus() != BorrowingStatus.BORROWED
                && borrowing.getStatus() != BorrowingStatus.OVERDUE) {

            throw new BookAlreadyReturnedException(
                    "This book has already been returned."
            );
        }

        Book book = borrowing.getBook();

        book.setAvailableCopies(book.getAvailableCopies() + 1);

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(BorrowingStatus.RETURNED);

        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }

    @Override
    public List<Borrowing> getBorrowingHistory(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId);
        }

        return borrowingRepository.findByMember_Id(memberId);
    }

    @Override
    public List<Borrowing> getActiveBorrowings() {
        return borrowingRepository.findByStatus(BorrowingStatus.BORROWED);
    }


    @Override
    public List<Borrowing> getOverdueBorrowings() {
        return borrowingRepository.findByStatus(
                BorrowingStatus.OVERDUE
        );
    }

    @Override
    public void updateOverdueBorrowings() {

        List<Borrowing> overdueBorrowings =
                borrowingRepository.findByStatusAndDueDateBefore(
                        BorrowingStatus.BORROWED,
                        LocalDate.now()
                );

        for (Borrowing borrowing : overdueBorrowings) {
            borrowing.setStatus(BorrowingStatus.OVERDUE);
        }

        borrowingRepository.saveAll(overdueBorrowings);
    }
    @Override
    public long getTotalBorrowedBooks() {
        return borrowingRepository.countByStatus(BorrowingStatus.BORROWED);
    }

    @Override
    public long getTotalReturnedBooks() {
        return borrowingRepository.countByStatus(BorrowingStatus.RETURNED);
    }

    @Override
    public long getTotalOverdueBooks() {
        return borrowingRepository.countByStatus(BorrowingStatus.OVERDUE);
    }

    @Override
    public long getTotalBorrowings() {
        return borrowingRepository.count();
    }

    @Override
    public List<Borrowing> getBorrowingHistoryByBook(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(
                    "Book not found with ID: " + bookId
            );
        }

        return borrowingRepository.findByBook_Id(bookId);


    }

    @Override
    public long getTotalBorrowingsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId
            );
        }

        return borrowingRepository.countByMember_Id(memberId);
    }

    @Override
    public long getActiveBorrowingsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId
            );
        }

        return borrowingRepository.countByMember_IdAndStatus(
                memberId,
                BorrowingStatus.BORROWED
        ) + borrowingRepository.countByMember_IdAndStatus(
                memberId,
                BorrowingStatus.OVERDUE
        );
    }

    @Override
    public List<Borrowing> getReturnedBorrowingsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId
            );
        }

        return borrowingRepository.findByMember_IdAndStatus(
                memberId,
                BorrowingStatus.RETURNED
        );
    }

    @Override
    public List<Borrowing> getOverdueBorrowingsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId
            );
        }

        return borrowingRepository.findByMember_IdAndStatus(
                memberId,
                BorrowingStatus.OVERDUE
        );
    }

    @Override
    public boolean hasActiveBorrowing(Long memberId, Long bookId) {

        return borrowingRepository.existsByMember_IdAndBook_IdAndStatusIn(
                memberId,
                bookId,
                List.of(
                        BorrowingStatus.BORROWED,
                        BorrowingStatus.OVERDUE
                )
        );
    }

    @Override
    public List<Borrowing> getBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date."
            );
        }

        return borrowingRepository.findByBorrowDateBetween(
                startDate,
                endDate
        );
    }


    @Override
    public long getTotalBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date."
            );
        }

        return borrowingRepository.countByBorrowDateBetween(
                startDate,
                endDate
        );
    }

    @Override
    public List<Borrowing> getReturnedBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date."
            );
        }

        return borrowingRepository.findByReturnDateBetween(
                startDate,
                endDate
        );
    }

    @Override
    public long getTotalReturnedBorrowingsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date."
            );
        }

        return borrowingRepository.countByReturnDateBetween(
                startDate,
                endDate
        );
    }


    @Override
    public BigDecimal calculateFine(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() ->
                        new BorrowingNotFoundException(
                                "Borrowing not found with ID: " + borrowingId
                        ));

        return fineCalculator.calculate(borrowing);
    }

    @Override
    public BigDecimal getTotalFineByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(
                    "Member not found with ID: " + memberId
            );
        }

        List<Borrowing> borrowings =
                borrowingRepository.findByMember_Id(memberId);

        return borrowings.stream()
                .map(fineCalculator::calculate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalLibraryFine() {

        List<Borrowing> borrowings = borrowingRepository.findAll();

        return borrowings.stream()
                .map(fineCalculator::calculate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
