
import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {

    // Get saved JWT token
    const [token, setToken] = useState(() => {
        return localStorage.getItem("token");
    });

    // Get saved user information safely
    const [user, setUser] = useState(() => {

        const savedUser = localStorage.getItem("user");

        if (!savedUser) {
            return null;
        }

        try {
            return JSON.parse(savedUser);
        } catch (error) {
            console.error("Invalid saved user data:", error);

            localStorage.removeItem("user");
            localStorage.removeItem("token");

            return null;
        }
    });

    // Store JWT token after successful login
    const login = (jwtToken, userData) => {

        localStorage.setItem("token", jwtToken);

        localStorage.setItem(
            "user",
            JSON.stringify(userData)
        );

        setToken(jwtToken);
        setUser(userData);
    };

    // Remove authentication data during logout
    const logout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("user");

        setToken(null);
        setUser(null);
    };

    // Check authentication status
    const isAuthenticated = Boolean(token && user);

    return (

        <AuthContext.Provider
            value={{
                token,
                user,
                login,
                logout,
                isAuthenticated
            }}
        >

            {children}

        </AuthContext.Provider>
    );
}

// Custom hook to access authentication context
export function useAuth() {

    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth must be used inside AuthProvider"
        );
    }

    return context;
}
