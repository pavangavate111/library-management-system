// Purpose: Keep all Member API calls in one place.


import api from "./api";

// Get all members
export const getAllMembers = (page = 0, size = 5, sort = "id") => {
    return api.get("/api/members", {
        params: { page, size, sort }
    });
};

// Get member by ID
export const getMemberById = (id) => {
    return api.get(`/api/members/${id}`);
};

// Register member
export const addMember = (member) => {
    return api.post("/api/members", member);
};

// Update member
export const updateMember = (id, member) => {
    return api.put(`/api/members/${id}`, member);
};

// Delete member
export const deleteMember = (id) => {
    return api.delete(`/api/members/${id}`);
};

// Search members by email
export const searchMembersByEmail = (email) => {
    return api.get("/api/members/search", {
        params: { email }
    });
};

// Search members by name
export const searchMembersByName = (name) => {
    return api.get("/api/members/search/name", {
        params: { name }
    });
};

// Get total members
export const getTotalMembers = () => {
    return api.get("/api/members/count");
};
