// Purpose: This file centralizes communication between React and your Spring Boot backend.

// Purpose:
//     - components – Reusable UI elements.
// - pages – Main application pages.
// - services – Backend API communication.
// import axios from "axios";
// React Router helps us navigate between pages without reloading the website.



import axios from "axios";

// Centralized Axios configuration
const api = axios.create({

    baseURL: "http://localhost:8080",

    headers: {
        "Content-Type": "application/json"
    }

});

// Automatically attach JWT token to every request
api.interceptors.request.use(

    (config) => {

        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }

);

// Handle unauthorized responses
api.interceptors.response.use(

    (response) => {
        return response;
    },

    (error) => {

        if (error.response?.status === 401) {

            localStorage.removeItem("token");
            localStorage.removeItem("user");

            // Do not redirect during login or registration
            const isAuthRequest =
                error.config?.url?.includes("/api/auth/login") ||
                error.config?.url?.includes("/api/auth/register");

            if (!isAuthRequest) {
                window.location.href = "/login";
            }
        }

        return Promise.reject(error);
    }

);

export default api;
