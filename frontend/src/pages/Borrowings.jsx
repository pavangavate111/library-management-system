
import { useEffect, useState } from "react";

import {
    getActiveBorrowings,
    getOverdueBorrowings,
    borrowBook,
    returnBook,
    getTotalBorrowedBooks,
    getTotalReturnedBooks,
    getTotalOverdueBooks,
    getTotalBorrowings
} from "../services/borrowingService";

import "./Borrowings.css";

function Borrowings() {

    const [borrowings, setBorrowings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [showForm, setShowForm] = useState(false);

    const [formData, setFormData] = useState({
        bookId: "",
        memberId: ""
    });

    const [overdueBorrowings, setOverdueBorrowings] = useState([]);
    const [showOverdue, setShowOverdue] = useState(false);

    const [totalBorrowings, setTotalBorrowings] = useState(0);
    const [totalBorrowed, setTotalBorrowed] = useState(0);
    const [totalReturned, setTotalReturned] = useState(0);
    const [totalOverdue, setTotalOverdue] = useState(0);

    // Fetch active borrowings
    const fetchBorrowings = async () => {
        try {
            const response = await getActiveBorrowings();

            setBorrowings(response.data);
            setError("");
        } catch (error) {
            console.error("Failed to load borrowings:", error);
            setError(
                error.response?.data?.message ||
                "Failed to load borrowings."
            );
        } finally {
            setLoading(false);
        }
    };

    // Fetch overdue borrowings
    const fetchOverdueBorrowings = async () => {
        try {
            const response = await getOverdueBorrowings();

            setOverdueBorrowings(response.data);
        } catch (error) {
            console.error("Failed to load overdue borrowings:", error);

            alert(
                error.response?.data?.message ||
                "Failed to load overdue borrowings."
            );
        }
    };

    // Fetch borrowing statistics
    const fetchBorrowingStatistics = async () => {
        try {
            const [
                borrowingsResponse,
                borrowedResponse,
                returnedResponse,
                overdueResponse
            ] = await Promise.all([
                getTotalBorrowings(),
                getTotalBorrowedBooks(),
                getTotalReturnedBooks(),
                getTotalOverdueBooks()
            ]);

            setTotalBorrowings(borrowingsResponse.data);
            setTotalBorrowed(borrowedResponse.data);
            setTotalReturned(returnedResponse.data);
            setTotalOverdue(overdueResponse.data);

        } catch (error) {
            console.error("Failed to load borrowing statistics:", error);
        }
    };

    // Load data when page opens
    useEffect(() => {
        fetchBorrowings();
        fetchBorrowingStatistics();
    }, []);

    // Handle form input changes
    const handleChange = (e) => {
        const { name, value } = e.target;

        setFormData((previousData) => ({
            ...previousData,
            [name]: value
        }));
    };

    // Reset borrowing form
    const resetForm = () => {
        setFormData({
            bookId: "",
            memberId: ""
        });

        setShowForm(false);
    };

    // Issue a book
    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await borrowBook({
                bookId: Number(formData.bookId),
                memberId: Number(formData.memberId)
            });

            alert("Book borrowed successfully!");

            resetForm();

            await fetchBorrowings();
            await fetchBorrowingStatistics();

        } catch (error) {
            console.error("Failed to borrow book:", error);

            alert(
                error.response?.data?.message ||
                "Failed to borrow book."
            );
        }
    };

    // Return a book
    const handleReturn = async (borrowingId) => {

        const confirmReturn = window.confirm(
            "Are you sure you want to return this book?"
        );

        if (!confirmReturn) return;

        try {
            await returnBook(borrowingId);

            alert("Book returned successfully!");

            await fetchBorrowings();
            await fetchBorrowingStatistics();

            if (showOverdue) {
                await fetchOverdueBorrowings();
            }

        } catch (error) {
            console.error("Failed to return book:", error);

            alert(
                error.response?.data?.message ||
                "Failed to return book."
            );
        }
    };

    if (loading) {
        return <p>Loading borrowings...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div className="borrowings">

            <h1>Manage Borrowings</h1>

            {/* Borrowing Statistics */}
            <div className="borrowing-statistics">

                <div className="stat-card">
                    <h3>Total Borrowings</h3>
                    <p>{totalBorrowings}</p>
                </div>

                <div className="stat-card">
                    <h3>Currently Borrowed</h3>
                    <p>{totalBorrowed}</p>
                </div>

                <div className="stat-card">
                    <h3>Total Returned</h3>
                    <p>{totalReturned}</p>
                </div>

                <div className="stat-card">
                    <h3>Total Overdue</h3>
                    <p>{totalOverdue}</p>
                </div>

            </div>

            {/* Issue New Book Button */}
            <button onClick={() => setShowForm(!showForm)}>
                {showForm ? "Close Form" : "Issue New Book"}
            </button>

            {/* Issue Book Form */}
            {showForm && (
                <form onSubmit={handleSubmit}>

                    <input
                        type="number"
                        name="bookId"
                        placeholder="Enter Book ID"
                        value={formData.bookId}
                        onChange={handleChange}
                        min="1"
                        required
                    />

                    <input
                        type="number"
                        name="memberId"
                        placeholder="Enter Member ID"
                        value={formData.memberId}
                        onChange={handleChange}
                        min="1"
                        required
                    />

                    <button type="submit">
                        Issue Book
                    </button>

                    <button
                        type="button"
                        onClick={resetForm}
                    >
                        Cancel
                    </button>

                </form>
            )}

            {/* Show/Hide Overdue Books Button */}
            <button
                onClick={() => {
                    if (!showOverdue) {
                        fetchOverdueBorrowings();
                    }

                    setShowOverdue(!showOverdue);
                }}
            >
                {showOverdue
                    ? "Hide Overdue Books"
                    : "Show Overdue Books"}
            </button>

            {/* Overdue Borrowings Table */}
            {showOverdue && (
                <div>

                    <h2>Overdue Borrowings</h2>

                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Book</th>
                            <th>Member</th>
                            <th>Borrow Date</th>
                            <th>Due Date</th>
                            <th>Status</th>
                            <th>Fine</th>
                        </tr>
                        </thead>

                        <tbody>
                        {overdueBorrowings.map((borrowing) => (
                            <tr key={borrowing.id}>
                                <td>{borrowing.id}</td>
                                <td>{borrowing.bookTitle}</td>
                                <td>{borrowing.memberName}</td>
                                <td>{borrowing.borrowDate}</td>
                                <td>{borrowing.dueDate}</td>
                                <td>{borrowing.status}</td>
                                <td>₹{borrowing.fineAmount}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                    {overdueBorrowings.length === 0 && (
                        <p>No overdue borrowings found.</p>
                    )}

                </div>
            )}

            {/* Active Borrowings Table */}
            <h2>Active Borrowings</h2>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Book</th>
                    <th>Member</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Fine</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>
                {borrowings.map((borrowing) => (
                    <tr key={borrowing.id}>
                        <td>{borrowing.id}</td>
                        <td>{borrowing.bookTitle}</td>
                        <td>{borrowing.memberName}</td>
                        <td>{borrowing.borrowDate}</td>
                        <td>{borrowing.dueDate}</td>
                        <td>{borrowing.status}</td>
                        <td>₹{borrowing.fineAmount}</td>

                        <td>
                            <button
                                onClick={() => handleReturn(borrowing.id)}
                            >
                                Return
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {borrowings.length === 0 && (
                <p>No active borrowings found.</p>
            )}

        </div>
    );
}

export default Borrowings;
