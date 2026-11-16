//Purpose: Keep all Book API calls in one place.

import api from "./api";

// Get all books
export const getAllBooks = (page = 0, size = 5, sort = "id") => {
    return api.get("/api/books", {
        params: { page, size, sort }
    });
};

// Get book by ID
export const getBookById = (id) => {
    return api.get(`/api/books/${id}`);
};

// Add book
export const addBook = (book) => {
    return api.post("/api/books", book);
};

// Update book
export const updateBook = (id, book) => {
    return api.put(`/api/books/${id}`, book);
};

// Delete book
export const deleteBook = (id) => {
    return api.delete(`/api/books/${id}`);
};

// Search books by title
export const searchBooks = (keyword, page = 0, size = 5) => {
    return api.get("/api/books/search", {
        params: { keyword, page, size }
    });
};

// Search by ISBN
export const searchBookByIsbn = (isbn) => {
    return api.get("/api/books/search/isbn", {
        params: { isbn }
    });
};

// Get total books
export const getTotalBooks = () => {
    return api.get("/api/books/count");
};

// Get available copies
export const getAvailableCopies = () => {
    return api.get("/api/books/available-count");
};

// Get issued copies
export const getIssuedCopies = () => {
    return api.get("/api/books/issued-count");
};

// Get books by category
export const getBooksByCategory = () => {
    return api.get("/api/books/category-count");
};

// Get books by publisher
export const getBooksByPublisher = (publisher) => {
    return api.get("/api/books/publisher", {
        params: { publisher }
    });
};

// Get available books
export const getAvailableBooks = (page = 0, size = 5) => {
    return api.get("/api/books/available", {
        params: { page, size }
    });
};
