
package com.library.lms.repository;

import com.library.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.library.lms.dto.CategoryStatisticsResponse;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    Page<Book> findByTitleContainingIgnoreCase(
            String title, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author, Pageable pageable);

    Optional<Book> findByIsbn(String isbn);


    @Query("SELECT COALESCE(SUM(b.availableCopies), 0) FROM Book b")
    long getTotalAvailableCopies();

    @Query("""
            SELECT COALESCE(SUM(b.totalCopies - b.availableCopies), 0)
            FROM Book b
            """)
    long getTotalIssuedCopies();


    @Query("""
    SELECT new com.library.lms.dto.CategoryStatisticsResponse(
        b.category, COUNT(b)
    )
    FROM Book b
    GROUP BY b.category
    """)
    List<CategoryStatisticsResponse> countBooksByCategory();

    List<Book> findByPublisherIgnoreCase(String publisher);

    Page<Book> findByAvailableCopiesGreaterThan(
            Integer copies,
            Pageable pageable
    );

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
