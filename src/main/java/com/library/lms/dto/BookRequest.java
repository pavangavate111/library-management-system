package com.library.lms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class BookRequest {

    @Pattern(
            regexp = "^(\\d{10}|\\d{13})$",
            message = "ISBN must contain exactly 10 or 13 digits"
    )
    private String isbn;


    @NotBlank(message = "Book title is required")
    @Size(
            min = 2,
            max = 150,
            message = "Book title must be between 2 and 150 characters"
    )
    private String title;

    @NotBlank(message = "Author name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Author name must be between 2 and 100 characters"
    )
    private String author;


    @Size(
            max = 50,
            message = "Category cannot exceed 50 characters"
    )
    private String category;

    @Size(
            max = 100,
            message = "Publisher name cannot exceed 100 characters"
    )
    private String publisher;

    @NotNull(message = "Total copies are required")
    @Min(value = 1, message = "Total copies must be at least 1")
    @Max(value = 10000, message = "Total copies cannot exceed 10000")
    private Integer totalCopies;

    @NotNull(message = "Available copies are required")
    @Min(value = 0, message = "Available copies cannot be negative")
    @Max(value = 10000, message = "Available copies cannot exceed 10000")
    private Integer availableCopies;


    private Long version;


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }



    /*

    A book cannot have more available copies than its total copies.
    this method executes automatically when @Valid triggers Bean Validation, usually during POST or PUT requests.
    //inside controller
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @Valid @RequestBody BookRequest request) {
        */

    @AssertTrue(message = "Available copies cannot exceed total copies")
    public boolean isAvailableCopiesValid() {

        if (availableCopies == null || totalCopies == null) {
            return true;
        }

        return availableCopies <= totalCopies;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


}
