
package com.library.lms.service.impl;

import com.library.lms.entity.Book;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowingRepository;
import com.library.lms.service.BookService;
import com.library.lms.exception.BookNotFoundException;
import com.library.lms.dto.CategoryStatisticsResponse;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;

    // Constructor Injection
    public BookServiceImpl(
            BookRepository bookRepository,
            BorrowingRepository borrowingRepository) {

        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
    }

    // Save a new book or update an existing book
    @Override
    public Book saveBook(Book book) {

        if (book.getId() == null) {

            if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
                throw new IllegalArgumentException(
                        "Book already exists with ISBN: " + book.getIsbn()
                );
            }

        } else {

            if (bookRepository.existsByIsbnAndIdNot(
                    book.getIsbn(), book.getId())) {

                throw new IllegalArgumentException(
                        "Another book already exists with ISBN: " + book.getIsbn()
                );
            }
        }

        return bookRepository.save(book);
    }

    // Retrieve all books
    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // Retrieve book by ID
    @Override
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book not found with ID: " + id
                        )
                );
    }

    // Delete book only when no borrowing history exists
    @Override
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book not found with ID: " + id
                        )
                );

        if (borrowingRepository.existsByBook_Id(id)) {
            throw new IllegalArgumentException(
                    "Cannot delete this book because borrowing history exists."
            );
        }

        bookRepository.delete(book);
    }

    @Override
    public Page<Book> searchBooks(String keyword, Pageable pageable) {

        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword, keyword, pageable
                );
    }

    @Override
    public Book searchBookByIsbn(String isbn) {

        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book not found with ISBN: " + isbn
                        )
                );
    }

    @Override
    public long getTotalBooks() {
        return bookRepository.count();
    }

    @Override
    public long getTotalAvailableCopies() {
        return bookRepository.getTotalAvailableCopies();
    }

    @Override
    public long getTotalIssuedCopies() {
        return bookRepository.getTotalIssuedCopies();
    }

    @Override
    public List<CategoryStatisticsResponse> getBooksByCategory() {
        return bookRepository.countBooksByCategory();
    }

    @Override
    public List<Book> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisherIgnoreCase(publisher);
    }

    @Override
    public Page<Book> getAvailableBooks(Pageable pageable) {
        return bookRepository.findByAvailableCopiesGreaterThan(0, pageable);
    }
}
