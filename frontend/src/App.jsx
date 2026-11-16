import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import { AuthProvider, useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

import Dashboard from "./pages/Dashboard";
import Books from "./pages/Books";
import Members from "./pages/Members";
import Borrowings from "./pages/Borrowings";
import BorrowingHistory from "./pages/BorrowingHistory";
import FineManagement from "./pages/FineManagement";

import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";

import Navbar from "./components/Navbar";

function PublicRoute({ children }) {
    const { isAuthenticated } = useAuth();

    return isAuthenticated
        ? <Navigate to="/" replace />
        : children;
}

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Navbar />

                <Routes>
                    {/* Authentication Routes */}
                    <Route
                        path="/login"
                        element={<PublicRoute><Login /></PublicRoute>}
                    />

                    <Route
                        path="/register"
                        element={<PublicRoute><Register /></PublicRoute>}
                    />

                    <Route
                        path="/forgot-password"
                        element={<PublicRoute><ForgotPassword /></PublicRoute>}
                    />

                    <Route
                        path="/reset-password"
                        element={<PublicRoute><ResetPassword /></PublicRoute>}
                    />

                    {/* Protected Routes */}
                    <Route element={<ProtectedRoute />}>
                        <Route path="/" element={<Dashboard />} />
                        <Route path="/books" element={<Books />} />
                        <Route path="/members" element={<Members />} />
                        <Route path="/borrowings" element={<Borrowings />} />
                        <Route path="/borrowing-history" element={<BorrowingHistory />} />
                        <Route path="/fine-management" element={<FineManagement />} />
                    </Route>

                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;