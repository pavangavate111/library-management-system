import api from "./api";

// Forgot Password
export const forgotPassword = (email) => {
    return api.post("/api/auth/forgot-password", { email });
};

// Reset Password
export const resetPassword = (token, newPassword) => {
    return api.post("/api/auth/reset-password", {
        token,
        newPassword
    });
};