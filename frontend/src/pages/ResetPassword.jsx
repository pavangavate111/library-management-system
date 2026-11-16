import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { resetPassword } from "../services/authService";
import "./PasswordReset.css";

function ResetPassword() {
    const location = useLocation();
    const navigate = useNavigate();

    // Read token from email reset link
    const token = new URLSearchParams(location.search).get("token");

    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    if (!token) {
        return (
            <div className="password-reset-page">
                <div className="password-reset-card">
                    <h2>Invalid or Missing Reset Link</h2>
                    <p>Please request a new password reset link.</p>
                    <Link to="/forgot-password">Try Again</Link>
                </div>
            </div>
        );
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (newPassword !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        if (newPassword.length < 8) {
            setError("Password must contain at least 8 characters.");
            return;
        }

        setLoading(true);

        try {
            await resetPassword(token, newPassword);

            alert("Password reset successfully. Please login with your new password.");

            navigate("/login", { replace: true });
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Unable to reset password. Your reset link may have expired."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="password-reset-page">
            <div className="password-reset-card">
                <h1>Reset Password</h1>
                <p>Create your new password.</p>

                <form onSubmit={handleSubmit}>
                    <label>New Password</label>

                    <input
                        type="password"
                        placeholder="Enter new password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        minLength={8}
                        required
                    />

                    <label>Confirm Password</label>

                    <input
                        type="password"
                        placeholder="Confirm new password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        minLength={8}
                        required
                    />

                    {error && (
                        <div className="reset-error">{error}</div>
                    )}

                    <button type="submit" disabled={loading}>
                        {loading ? "Resetting..." : "Reset Password"}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default ResetPassword;