
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function ProtectedRoute() {

    const { isAuthenticated, user } = useAuth();

    // Redirect unauthenticated users to Login
    if (!isAuthenticated || !user) {
        return <Navigate to="/login" replace />;
    }

    // Allow authenticated users to access protected pages
    return <Outlet />;
}

export default ProtectedRoute;
