
import "./Navbar.css";

import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {

    const { isAuthenticated, user, logout } = useAuth();

    const navigate = useNavigate();

    // Logout user
    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    // Active navigation link styling
    const getNavLinkClass = ({ isActive }) => {
        return isActive ? "active" : "";
    };

    return (
        <nav>

            {isAuthenticated ? (

                <>

                    {/* Main Navigation */}
                    <NavLink to="/" className={getNavLinkClass}>
                        Dashboard
                    </NavLink>

                    <NavLink to="/books" className={getNavLinkClass}>
                        Books
                    </NavLink>

                    <NavLink to="/members" className={getNavLinkClass}>
                        Members
                    </NavLink>

                    <NavLink to="/borrowings" className={getNavLinkClass}>
                        Borrowings
                    </NavLink>

                    <NavLink
                        to="/borrowing-history"
                        className={getNavLinkClass}
                    >
                        Borrowing History
                    </NavLink>

                    <NavLink
                        to="/fine-management"
                        className={getNavLinkClass}
                    >
                        Fine Management
                    </NavLink>

                    {/* User Information */}
                    <div className="navbar-user">

                        <span className="welcome-text">
                            Welcome, {user?.name || "User"}
                        </span>

                        <span
                            className={`role-badge ${
                                user?.role?.toLowerCase() || "user"
                            }`}
                        >
                            {user?.role || "USER"}
                        </span>

                    </div>

                    {/* Logout */}
                    <button onClick={handleLogout}>
                        Logout
                    </button>

                </>

            ) : (

                <>

                    <NavLink to="/login" className={getNavLinkClass}>
                        Login
                    </NavLink>

                    <NavLink to="/register" className={getNavLinkClass}>
                        Register
                    </NavLink>

                </>

            )}

        </nav>
    );
}

export default Navbar;
