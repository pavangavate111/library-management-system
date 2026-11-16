
package com.library.lms.dto;

public class CategoryStatisticsResponse {

    private String category;
    private Long totalBooks;

    public CategoryStatisticsResponse(String category, Long totalBooks) {
        this.category = category;
        this.totalBooks = totalBooks;
    }

    public String getCategory() {
        return category;
    }

    public Long getTotalBooks() {
        return totalBooks;
    }
}
