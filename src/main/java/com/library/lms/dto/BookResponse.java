
package com.library.lms.dto;

import java.time.LocalDateTime;

public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDateTime createdAt;

    private Long version;


    public BookResponse(Long id, String isbn, String title,
                        String author, String category,
                        String publisher, Integer totalCopies,
                        Integer availableCopies,
                        LocalDateTime createdAt,
                        Long version) {

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.createdAt = createdAt;
        this.version = version;
    }

    public BookResponse() {

    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getVersion() {
        return version;
    }
}
