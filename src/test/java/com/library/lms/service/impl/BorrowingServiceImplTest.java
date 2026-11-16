package com.library.lms.service.impl;

import com.library.lms.entity.Book;
import com.library.lms.entity.Borrowing;
import com.library.lms.entity.BorrowingStatus;
import com.library.lms.entity.Member;

import com.library.lms.exception.*;

import com.library.lms.repository.BorrowingRepository;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.MemberRepository;

import com.library.lms.service.FineCalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceImplTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FineCalculator fineCalculator;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Test
    void shouldThrowExceptionWhenBookDoesNotExist() {

        when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenMemberDoesNotExist() {

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(new Book()));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenBookIsNotAvailable() {

        Book book = new Book();
        book.setAvailableCopies(0);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsById(1L))
                .thenReturn(true);

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(false);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.BORROWED
        )).thenReturn(0L);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.OVERDUE
        )).thenReturn(0L);

        assertThrows(
                BookNotAvailableException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }

    @Test
    void shouldBorrowBookSuccessfully() {

        Book book = new Book();
        book.setAvailableCopies(5);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsById(1L))
                .thenReturn(true);

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(false);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.BORROWED
        )).thenReturn(0L);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.OVERDUE
        )).thenReturn(0L);

        when(borrowingRepository.save(any(Borrowing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Borrowing result = borrowingService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(4, book.getAvailableCopies());

        verify(bookRepository).save(book);
        verify(borrowingRepository).save(any(Borrowing.class));

    }

    @Test
    void shouldThrowExceptionWhenMemberBorrowsSameBookAgain() {

        Book book = new Book();
        book.setAvailableCopies(5);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(true);

        assertThrows(
                DuplicateBorrowingException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }


    @Test
    void shouldThrowExceptionWhenBorrowingLimitIsExceeded() {

        Book book = new Book();
        book.setAvailableCopies(5);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(memberRepository.existsById(1L))
                .thenReturn(true);

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(false);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.BORROWED
        )).thenReturn(5L);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.OVERDUE
        )).thenReturn(0L);

        assertThrows(
                BorrowingLimitExceededException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }


    @Test
    void shouldReturnBookSuccessfully() {

        Book book = new Book();
        book.setAvailableCopies(4);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setStatus(BorrowingStatus.BORROWED);

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.of(borrowing));

        when(borrowingRepository.save(any(Borrowing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Borrowing result = borrowingService.returnBook(1L);

        assertNotNull(result);
        assertEquals(BorrowingStatus.RETURNED, result.getStatus());
        assertEquals(5, book.getAvailableCopies());
        assertEquals(LocalDate.now(), result.getReturnDate());

        verify(bookRepository).save(book);
        verify(borrowingRepository).save(borrowing);
    }


    @Test
    void shouldThrowExceptionWhenBorrowingRecordDoesNotExist() {

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BorrowingNotFoundException.class,
                () -> borrowingService.returnBook(1L)
        );
    }


    @Test
    void shouldThrowExceptionWhenBookIsAlreadyReturned() {

        Borrowing borrowing = new Borrowing();
        borrowing.setStatus(BorrowingStatus.RETURNED);

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.of(borrowing));

        assertThrows(
                BookAlreadyReturnedException.class,
                () -> borrowingService.returnBook(1L)
        );
    }

    @Test
    void shouldReturnOverdueBookSuccessfully() {

        Book book = new Book();
        book.setAvailableCopies(2);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setStatus(BorrowingStatus.OVERDUE);

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.of(borrowing));

        when(borrowingRepository.save(any(Borrowing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Borrowing result = borrowingService.returnBook(1L);

        assertEquals(BorrowingStatus.RETURNED, result.getStatus());
        assertEquals(3, book.getAvailableCopies());
        assertEquals(LocalDate.now(), result.getReturnDate());

        verify(bookRepository).save(book);
        verify(borrowingRepository).save(borrowing);
    }
    @Test
    void shouldThrowExceptionWhenBorrowingLimitIncludesOverdueBooks() {

        Book book = new Book();
        book.setAvailableCopies(5);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(false);

        when(memberRepository.existsById(1L))
                .thenReturn(true);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.BORROWED
        )).thenReturn(3L);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.OVERDUE
        )).thenReturn(2L);

        assertThrows(
                BorrowingLimitExceededException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );
    }

    @Test
    void shouldNotDecreaseBookCopiesWhenBookIsUnavailable() {

        Book book = new Book();
        book.setAvailableCopies(0);

        Member member = new Member();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(borrowingRepository
                .existsByMember_IdAndBook_IdAndStatusIn(
                        1L,
                        1L,
                        List.of(BorrowingStatus.BORROWED, BorrowingStatus.OVERDUE)
                ))
                .thenReturn(false);

        when(memberRepository.existsById(1L))
                .thenReturn(true);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.BORROWED
        )).thenReturn(0L);

        when(borrowingRepository.countByMember_IdAndStatus(
                1L, BorrowingStatus.OVERDUE
        )).thenReturn(0L);

        assertThrows(
                BookNotAvailableException.class,
                () -> borrowingService.borrowBook(1L, 1L)
        );

        assertEquals(0, book.getAvailableCopies());

        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }
    @Test
    void shouldNotIncreaseBookCopiesWhenBorrowingRecordDoesNotExist() {

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BorrowingNotFoundException.class,
                () -> borrowingService.returnBook(1L)
        );

        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }

    @Test
    void shouldNotIncreaseBookCopiesWhenBookIsAlreadyReturned() {

        Book book = new Book();
        book.setAvailableCopies(5);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setStatus(BorrowingStatus.RETURNED);

        when(borrowingRepository.findById(1L))
                .thenReturn(Optional.of(borrowing));

        assertThrows(
                BookAlreadyReturnedException.class,
                () -> borrowingService.returnBook(1L)
        );

        assertEquals(5, book.getAvailableCopies());

        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }

    
    }
