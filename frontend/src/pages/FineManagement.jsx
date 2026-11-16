
import { useState, useEffect } from "react";

import {
    calculateFine,
    getTotalFineByMember,
    getTotalLibraryFine
} from "../services/borrowingService";

import "./FineManagement.css";

function FineManagement() {

    const [borrowingId, setBorrowingId] = useState("");
    const [memberId, setMemberId] = useState("");

    const [borrowingFine, setBorrowingFine] = useState(null);
    const [memberFine, setMemberFine] = useState(null);
    const [totalLibraryFine, setTotalLibraryFine] = useState(0);

    const [loading, setLoading] = useState(false);
    const [libraryFineLoading, setLibraryFineLoading] = useState(true);

    const [borrowingError, setBorrowingError] = useState("");
    const [memberError, setMemberError] = useState("");
    const [libraryFineError, setLibraryFineError] = useState("");

    // Fetch total library fine when page loads
    useEffect(() => {

        const fetchTotalLibraryFine = async () => {

            try {
                const response = await getTotalLibraryFine();

                setTotalLibraryFine(response.data);

            } catch (error) {
                console.error("Failed to fetch total library fine:", error);

                setLibraryFineError(
                    error.response?.data?.message ||
                    "Failed to fetch total library fine."
                );

            } finally {
                setLibraryFineLoading(false);
            }
        };

        fetchTotalLibraryFine();

    }, []);

    // Calculate fine for a specific borrowing
    const handleBorrowingFine = async (e) => {

        e.preventDefault();

        if (!borrowingId || Number(borrowingId) <= 0) {
            setBorrowingError("Please enter a valid Borrowing ID.");
            setBorrowingFine(null);
            return;
        }

        setLoading(true);
        setBorrowingError("");
        setBorrowingFine(null);

        try {
            const response = await calculateFine(Number(borrowingId));

            setBorrowingFine(response.data);

        } catch (error) {
            console.error("Failed to calculate borrowing fine:", error);

            setBorrowingError(
                error.response?.data?.message ||
                "Failed to calculate borrowing fine."
            );

        } finally {
            setLoading(false);
        }
    };

    // Get total fine for a specific member
    const handleMemberFine = async (e) => {

        e.preventDefault();

        if (!memberId || Number(memberId) <= 0) {
            setMemberError("Please enter a valid Member ID.");
            setMemberFine(null);
            return;
        }

        setLoading(true);
        setMemberError("");
        setMemberFine(null);

        try {
            const response = await getTotalFineByMember(Number(memberId));

            setMemberFine(response.data);

        } catch (error) {
            console.error("Failed to fetch member fine:", error);

            setMemberError(
                error.response?.data?.message ||
                "Failed to fetch member fine."
            );

        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fine-management">

            <h1>Fine Management</h1>

            {/* Total Library Fine */}
            <div className="fine-card">

                <h2>Total Library Fine</h2>

                {libraryFineLoading ? (
                    <p>Loading total library fine...</p>
                ) : libraryFineError ? (
                    <p className="error-message">{libraryFineError}</p>
                ) : (
                    <p>₹{totalLibraryFine ?? 0}</p>
                )}

            </div>

            {/* Calculate Borrowing Fine */}
            <div className="fine-section">

                <h2>Calculate Borrowing Fine</h2>

                <form onSubmit={handleBorrowingFine}>

                    <input
                        type="number"
                        placeholder="Enter Borrowing ID"
                        value={borrowingId}
                        onChange={(e) => {
                            setBorrowingId(e.target.value);
                            setBorrowingError("");
                            setBorrowingFine(null);
                        }}
                        min="1"
                        required
                    />

                    <button type="submit" disabled={loading}>
                        {loading ? "Calculating..." : "Calculate Fine"}
                    </button>

                </form>

                {borrowingError && (
                    <p className="error-message">
                        {borrowingError}
                    </p>
                )}

                {borrowingFine !== null && (
                    <p>
                        Borrowing Fine: ₹{borrowingFine}
                    </p>
                )}

            </div>

            {/* Calculate Member Fine */}
            <div className="fine-section">

                <h2>Member Total Fine</h2>

                <form onSubmit={handleMemberFine}>

                    <input
                        type="number"
                        placeholder="Enter Member ID"
                        value={memberId}
                        onChange={(e) => {
                            setMemberId(e.target.value);
                            setMemberError("");
                            setMemberFine(null);
                        }}
                        min="1"
                        required
                    />

                    <button type="submit" disabled={loading}>
                        {loading ? "Loading..." : "Get Member Fine"}
                    </button>

                </form>

                {memberError && (
                    <p className="error-message">
                        {memberError}
                    </p>
                )}

                {memberFine !== null && (
                    <p>
                        Member Fine: ₹{memberFine}
                    </p>
                )}

            </div>

        </div>
    );
}

export default FineManagement;
