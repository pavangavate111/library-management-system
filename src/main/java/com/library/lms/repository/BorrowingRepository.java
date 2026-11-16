
package com.library.lms.repository;

import com.library.lms.entity.Borrowing;
import com.library.lms.entity.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository
        extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByMember_Id(Long memberId);

    List<Borrowing> findByStatus(BorrowingStatus status);

    List<Borrowing> findByStatusAndDueDateBefore(
            BorrowingStatus status,
            LocalDate date
    );

    long countByStatus(BorrowingStatus status);

    long count();

    List<Borrowing> findByBook_Id(Long bookId);

    boolean existsByBook_Id(Long bookId);

    long countByMember_Id(Long memberId);

    long countByMember_IdAndStatus(
            Long memberId,
            BorrowingStatus status
    );

    List<Borrowing> findByMember_IdAndStatus(
            Long memberId,
            BorrowingStatus status
    );

    boolean existsByMember_IdAndBook_IdAndStatusIn(
            Long memberId,
            Long bookId,
            List<BorrowingStatus> statuses
    );

    List<Borrowing> findByBorrowDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByBorrowDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Borrowing> findByReturnDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByReturnDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByMember_IdAndStatusIn(
            Long memberId,
            List<BorrowingStatus> statuses
    );
}
