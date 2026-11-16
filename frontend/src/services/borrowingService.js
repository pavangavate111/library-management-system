// Purpose: Keep all Borrowing API calls in one place.

import api from "./api";

// Borrow a book
export const borrowBook = (borrowing) => {
    return api.post("/api/borrowings/borrow", borrowing);
};

// Return a book
export const returnBook = (borrowingId) => {
    return api.put(`/api/borrowings/return/${borrowingId}`);
};

// Get borrowing history by member
export const getBorrowingHistory = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/history`);
};

// Get active borrowings
export const getActiveBorrowings = () => {
    return api.get("/api/borrowings/active");
};

// Get overdue borrowings
export const getOverdueBorrowings = () => {
    return api.get("/api/borrowings/overdue");
};

// Update overdue statuses
export const updateOverdueBorrowings = () => {
    return api.put("/api/borrowings/update-overdue");
};

// Get borrowing counts
export const getTotalBorrowedBooks = () => {
    return api.get("/api/borrowings/count/borrowed");
};

export const getTotalReturnedBooks = () => {
    return api.get("/api/borrowings/count/returned");
};

export const getTotalOverdueBooks = () => {
    return api.get("/api/borrowings/count/overdue");
};

export const getTotalBorrowings = () => {
    return api.get("/api/borrowings/count");
};

// Get borrowing history by book
export const getBorrowingHistoryByBook = (bookId) => {
    return api.get(`/api/borrowings/book/${bookId}/history`);
};

// Member-specific counts and records
export const getTotalBorrowingsByMember = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/count`);
};

export const getActiveBorrowingsByMember = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/active-count`);
};

export const getReturnedBorrowingsByMember = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/returned`);
};

export const getOverdueBorrowingsByMember = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/overdue`);
};

// Date-range operations
export const getBorrowingsBetweenDates = (startDate, endDate) => {
    return api.get("/api/borrowings/date-range", {
        params: { startDate, endDate }
    });
};

export const getTotalBorrowingsBetweenDates = (startDate, endDate) => {
    return api.get("/api/borrowings/date-range/count", {
        params: { startDate, endDate }
    });
};

export const getReturnedBorrowingsBetweenDates = (startDate, endDate) => {
    return api.get("/api/borrowings/returned/date-range", {
        params: { startDate, endDate }
    });
};

export const getTotalReturnedBorrowingsBetweenDates = (startDate, endDate) => {
    return api.get("/api/borrowings/returned/date-range/count", {
        params: { startDate, endDate }
    });
};

// Fine calculations
export const calculateFine = (borrowingId) => {
    return api.get(`/api/borrowings/${borrowingId}/fine`);
};

export const getTotalFineByMember = (memberId) => {
    return api.get(`/api/borrowings/member/${memberId}/total-fine`);
};

export const getTotalLibraryFine = () => {
    return api.get("/api/borrowings/total-fine");
};
