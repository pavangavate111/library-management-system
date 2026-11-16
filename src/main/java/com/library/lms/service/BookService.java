
package com.library.lms.service;

import com.library.lms.entity.Book;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.library.lms.dto.CategoryStatisticsResponse;

public interface BookService {

    // Add a new book or update an existing book
    Book saveBook(Book book);

    // Retrieve all books


    Page<Book> getAllBooks(Pageable pageable);

    // Retrieve a book using its ID
    Book getBookById(Long id);

    // Delete a book using its ID
    void deleteBook(Long id);

//    serach book by name autrhor what u want
    Page<Book> searchBooks(String keyword, Pageable pageable);

    //serach by isbn
    Book searchBookByIsbn(String isbn);

    // count books
    long getTotalBooks();

    long getTotalAvailableCopies();

    long getTotalIssuedCopies();

    List<CategoryStatisticsResponse> getBooksByCategory();

    List<Book> getBooksByPublisher(String publisher);

    Page<Book> getAvailableBooks(Pageable pageable);
}
