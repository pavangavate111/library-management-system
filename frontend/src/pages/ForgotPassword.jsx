import { useState } from "react";
import { Link } from "react-router-dom";
import { forgotPassword } from "../services/authService";
import "./PasswordReset.css";

function ForgotPassword() {
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        setLoading(true);
        setError("");
        setMessage("");

        try {
            await forgotPassword(email);

            setMessage(
                "If the email is registered, a password reset link will be sent."
            );
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Unable to process your request. Please try again."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="password-reset-page">
            <div className="password-reset-card">
                <h1>Forgot Password?</h1>

                <p>
                    Enter your registered email to receive a password
                    reset link.
                </p>

                <form onSubmit={handleSubmit}>
                    <label>Email Address</label>

                    <input
                        type="email"
                        placeholder="Enter registered email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    {error && (
                        <div className="reset-error">{error}</div>
                    )}

                    {message && (
                        <div className="reset-success">{message}</div>
                    )}

                    <button type="submit" disabled={loading}>
                        {loading ? "Sending..." : "Send Reset Link"}
                    </button>
                </form>

                <Link to="/login">Back to Login</Link>
            </div>
        </div>
    );
}

export default ForgotPassword;