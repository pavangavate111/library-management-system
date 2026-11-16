
package com.library.lms.controller;

import com.library.lms.exception.BookVersionConflictException;
import jakarta.validation.Valid;
import com.library.lms.entity.Book;
import com.library.lms.service.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.library.lms.dto.BookRequest;
import com.library.lms.mapper.BookMapper;
import jakarta.validation.Valid;

import com.library.lms.dto.BookResponse;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import com.library.lms.dto.CategoryStatisticsResponse;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookMapper bookMapper;
    private final BookService bookService;

    // Constructor Injection
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // Add a new book
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @Valid @RequestBody BookRequest request) {

        Book book = bookMapper.toEntity(request);

        Book savedBook = bookService.saveBook(book);

        BookResponse response = bookMapper.toResponse(savedBook);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Get all books

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @PageableDefault(size = 3, sort = "id")
            Pageable pageable) {

        Page<BookResponse> books = bookService
                .getAllBooks(pageable)
                .map(bookMapper::toResponse);

        return ResponseEntity.ok(books);
    }


    // Get book by ID

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id) {

        Book book = bookService.getBookById(id);

        BookResponse response = bookMapper.toResponse(book);

        return ResponseEntity.ok(response);
    }


    // Update book
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        Book existingBook = bookService.getBookById(id);

        if (!Objects.equals(request.getVersion(), existingBook.getVersion())) {
            throw new BookVersionConflictException(
                    "This book was modified by another user. Please refresh and try again."
            );
        }

        Book updatedBook = bookMapper.updateEntity(request, existingBook);

        Book savedBook = bookService.saveBook(updatedBook);

        BookResponse response = bookMapper.toResponse(savedBook);

        return ResponseEntity.ok(response);
    }


    // Delete book
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    // search by title

    @GetMapping("/search")

    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam String keyword,
            @PageableDefault(size = 5, sort = "id")
            Pageable pageable) {

        Page<BookResponse> books = bookService
                .searchBooks(keyword, pageable)
                .map(bookMapper::toResponse);

        return ResponseEntity.ok(books);
    }

    // serach by isbn

    @GetMapping("/search/isbn")
    public ResponseEntity<BookResponse> searchByIsbn(
            @RequestParam String isbn) {

        Book book = bookService.searchBookByIsbn(isbn);

        return ResponseEntity.ok(bookMapper.toResponse(book));
    }

    //count books

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalBooks() {
        return ResponseEntity.ok(bookService.getTotalBooks());
    }


    @GetMapping("/available-count")
    public ResponseEntity<Long> getTotalAvailableCopies() {
        return ResponseEntity.ok(
                bookService.getTotalAvailableCopies()
        );
    }



    @GetMapping("/issued-count")
    public ResponseEntity<Long> getTotalIssuedCopies() {
        return ResponseEntity.ok(
                bookService.getTotalIssuedCopies()
        );
    }


    @GetMapping("/category-count")
    public ResponseEntity<List<CategoryStatisticsResponse>> getBooksByCategory() {

        return ResponseEntity.ok(
                bookService.getBooksByCategory()
        );
    }


    @GetMapping("/publisher")
    public ResponseEntity<List<BookResponse>> getBooksByPublisher(
            @RequestParam String publisher) {

        List<BookResponse> books = bookService
                .getBooksByPublisher(publisher)
                .stream()
                .map(bookMapper::toResponse)
                .toList();

        return ResponseEntity.ok(books);
    }


    @GetMapping("/available")
    public ResponseEntity<Page<BookResponse>> getAvailableBooks(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        Page<BookResponse> books = bookService
                .getAvailableBooks(pageable)
                .map(bookMapper::toResponse);

        return ResponseEntity.ok(books);

    }
}
