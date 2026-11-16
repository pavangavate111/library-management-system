
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

import api from "../services/api";

import "./Register.css";

function Register() {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleRegister = async (e) => {

        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            await api.post("/api/auth/register", {
                name,
                email,
                password
            });

            navigate("/login");

        } catch (error) {

            setError(
                error.response?.data?.message ||
                "Registration failed. Please try again."
            );

        } finally {

            setLoading(false);

        }
    };

    return (

        <div className="register-page">

            <div className="register-card">

                <div className="register-header">

                    <div className="register-icon">
                        📚
                    </div>

                    <h1>Create Account</h1>

                    <p>Join our Library Management System</p>

                </div>

                <form onSubmit={handleRegister}>

                    <div className="register-form-group">

                        <label>Full Name</label>

                        <input
                            type="text"
                            placeholder="Enter your full name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />

                    </div>

                    <div className="register-form-group">

                        <label>Email Address</label>

                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />

                    </div>

                    <div className="register-form-group">

                        <label>Password</label>

                        <div className="register-password-wrapper">

                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder="Create a password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />

                            <button
                                type="button"
                                className="register-password-toggle"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? "Hide" : "Show"}
                            </button>

                        </div>

                    </div>

                    {error && (

                        <div className="register-error">
                            {error}
                        </div>

                    )}

                    <button
                        type="submit"
                        className="register-button"
                        disabled={loading}
                    >
                        {loading ? "Creating Account..." : "Register"}
                    </button>

                </form>

                <div className="register-footer">

                    <p>
                        Already have an account?
                        {" "}
                        <Link to="/login">Login</Link>
                    </p>

                </div>

            </div>

        </div>

    );
}

export default Register;
