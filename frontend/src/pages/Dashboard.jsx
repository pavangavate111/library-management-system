
import { useState, useEffect } from "react";

import { getTotalBooks } from "../services/bookService";
import { getTotalMembers } from "../services/memberService";

import {
    getActiveBorrowings,
    getOverdueBorrowings,
    getTotalLibraryFine
} from "../services/borrowingService";

import "./Dashboard.css";

function Dashboard() {

    const [totalBooks, setTotalBooks] = useState(0);
    const [totalMembers, setTotalMembers] = useState(0);
    const [activeBorrowings, setActiveBorrowings] = useState(0);
    const [overdueBooks, setOverdueBooks] = useState(0);
    const [totalFine, setTotalFine] = useState(0);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Fetch all dashboard statistics
    useEffect(() => {

        const fetchDashboardData = async () => {

            try {

                const [
                    booksResponse,
                    membersResponse,
                    activeBorrowingsResponse,
                    overdueBorrowingsResponse,
                    fineResponse
                ] = await Promise.all([
                    getTotalBooks(),
                    getTotalMembers(),
                    getActiveBorrowings(),
                    getOverdueBorrowings(),
                    getTotalLibraryFine()
                ]);

                setTotalBooks(booksResponse.data);
                setTotalMembers(membersResponse.data);

                setActiveBorrowings(
                    activeBorrowingsResponse.data.length
                );

                setOverdueBooks(
                    overdueBorrowingsResponse.data.length
                );

                setTotalFine(fineResponse.data);

            } catch (error) {

                console.error("Failed to load dashboard:", error);

                setError(
                    error.response?.data?.message ||
                    "Failed to load dashboard statistics."
                );

            } finally {

                setLoading(false);

            }
        };

        fetchDashboardData();

    }, []);

    // Loading state
    if (loading) {
        return (
            <div className="dashboard">
                <h2>Loading Dashboard...</h2>
            </div>
        );
    }

    // Error state
    if (error) {
        return (
            <div className="dashboard">
                <h2>Dashboard</h2>
                <p className="error-message">{error}</p>
            </div>
        );
    }

    return (

        <div className="dashboard">

            <h1>Library Management System</h1>

            <p>Welcome to your Library Dashboard!</p>

            {/* Dashboard Statistics */}
            <div className="dashboard-stats">

                <div className="dashboard-card">
                    <h3>Total Books</h3>
                    <p>{totalBooks}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Total Members</h3>
                    <p>{totalMembers}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Active Borrowings</h3>
                    <p>{activeBorrowings}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Overdue Books</h3>
                    <p>{overdueBooks}</p>
                </div>

                <div className="dashboard-card">
                    <h3>Total Library Fine</h3>
                    <p>₹{totalFine}</p>
                </div>

            </div>

        </div>

    );
}

export default Dashboard;
