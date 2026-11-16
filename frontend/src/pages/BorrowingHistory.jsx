import "./BorrowingHistory.css";

import { useState } from "react";
import { getBorrowingHistory } from "../services/borrowingService";

function BorrowingHistory() {

    const [memberId, setMemberId] = useState("");
    const [borrowings, setBorrowings] = useState([]);

    const [loading, setLoading] = useState(false);
    const [searched, setSearched] = useState(false);
    const [error, setError] = useState("");

    // Fetch borrowing history by Member ID
    const handleSearch = async (e) => {

        e.preventDefault();

        if (!memberId || Number(memberId) <= 0) {
            setError("Please enter a valid Member ID.");
            setBorrowings([]);
            setSearched(false);
            return;
        }

        setLoading(true);
        setError("");
        setBorrowings([]);
        setSearched(false);

        try {
            const response = await getBorrowingHistory(Number(memberId));

            setBorrowings(response.data);
            setSearched(true);

        } catch (error) {
            console.error("Failed to fetch borrowing history:", error);

            setError(
                error.response?.data?.message ||
                "Failed to fetch borrowing history."
            );

            setBorrowings([]);

        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="borrowing-history">

            <h1>Borrowing History</h1>

            {/* Search Borrowing History */}
            <form
                className="history-search"
                onSubmit={handleSearch}
            >

                <input
                    type="number"
                    placeholder="Enter Member ID"
                    value={memberId}
                    onChange={(e) => setMemberId(e.target.value)}
                    min="1"
                    required
                />

                <button type="submit" disabled={loading}>
                    {loading ? "Searching..." : "Search"}
                </button>

            </form>

            {/* Loading Message */}
            {loading && (
                <p>Loading borrowing history...</p>
            )}

            {/* Error Message */}
            {!loading && error && (
                <p className="error-message">
                    {error}
                </p>
            )}

            {/* Borrowing History Table */}
            {!loading && !error && borrowings.length > 0 && (

                <div>

                    <h2>Member Borrowing Records</h2>

                    <table>

                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Book</th>
                            <th>Borrow Date</th>
                            <th>Due Date</th>
                            <th>Return Date</th>
                            <th>Status</th>
                            <th>Fine</th>
                        </tr>
                        </thead>

                        <tbody>

                        {borrowings.map((borrowing) => (

                            <tr key={borrowing.id}>

                                <td>{borrowing.id}</td>

                                <td>{borrowing.bookTitle}</td>

                                <td>{borrowing.borrowDate}</td>

                                <td>{borrowing.dueDate}</td>

                                <td>
                                    {borrowing.returnDate || "-"}
                                </td>

                                <td>{borrowing.status}</td>

                                <td>
                                    ₹{borrowing.fineAmount ?? 0}
                                </td>

                            </tr>

                        ))}

                        </tbody>

                    </table>

                </div>

            )}

            {/* No Records Found */}
            {!loading && searched && !error && borrowings.length === 0 && (
                <p>No borrowing history found for this member.</p>
            )}

        </div>
    );
}

export default BorrowingHistory;