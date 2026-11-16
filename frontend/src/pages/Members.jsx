
import { useEffect, useState } from "react";

import {
    getAllMembers,
    addMember,
    updateMember,
    deleteMember
} from "../services/memberService";

import { useAuth } from "../context/AuthContext";
import "./Members.css";

function Members() {

    const { user } = useAuth();
    const isAdmin = user?.role === "ADMIN";

    const [members, setMembers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [showForm, setShowForm] = useState(false);
    const [editingMemberId, setEditingMemberId] = useState(null);

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        phone: "",
        address: ""
    });

    const resetForm = () => {
        setFormData({
            name: "",
            email: "",
            phone: "",
            address: ""
        });

        setEditingMemberId(null);
        setShowForm(false);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {

        e.preventDefault();

        if (!isAdmin) return;

        addMember(formData)
            .then(() => {
                alert("Member added successfully!");
                resetForm();
                return getAllMembers();
            })
            .then((response) => {
                setMembers(response.data.content);
            })
            .catch((error) => {
                console.error(error);
                alert(
                    error.response?.data?.message ||
                    "Failed to add member. Please check the details."
                );
            });
    };

    const handleEdit = (member) => {

        if (!isAdmin) return;

        setEditingMemberId(member.id);

        setFormData({
            name: member.name,
            email: member.email,
            phone: member.phone,
            address: member.address
        });

        setShowForm(true);
    };

    const handleUpdate = (e) => {

        e.preventDefault();

        if (!isAdmin) return;

        updateMember(editingMemberId, formData)
            .then(() => {
                alert("Member updated successfully!");
                resetForm();
                return getAllMembers();
            })
            .then((response) => {
                setMembers(response.data.content);
            })
            .catch((error) => {
                console.error(error);
                alert(
                    error.response?.data?.message ||
                    "Failed to update member. Please check the details."
                );
            });
    };

    const handleDelete = (id) => {

        if (!isAdmin) return;

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this member?"
        );

        if (!confirmDelete) return;

        deleteMember(id)
            .then(() => {
                alert("Member deleted successfully!");
                return getAllMembers();
            })
            .then((response) => {
                setMembers(response.data.content);
            })
            .catch((error) => {
                console.error(error);

                alert(
                    error.response?.data?.message ||
                    "Failed to delete member."
                );
            });
    };

    useEffect(() => {

        setLoading(true);

        getAllMembers()
            .then((response) => {
                setMembers(response.data.content);
                setLoading(false);
            })
            .catch((error) => {
                console.error(error);
                setError("Failed to load members.");
                setLoading(false);
            });

    }, []);

    if (loading) {
        return <p>Loading members...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (

        <div className="members">

            <h1>Manage Members</h1>

            {isAdmin && (
                <button
                    onClick={() => {
                        if (showForm) {
                            resetForm();
                        } else {
                            setShowForm(true);
                        }
                    }}
                >
                    Add New Member
                </button>
            )}

            {isAdmin && showForm && (

                <form onSubmit={editingMemberId ? handleUpdate : handleSubmit}>

                    <input
                        type="text"
                        name="name"
                        placeholder="Member Name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="email"
                        name="email"
                        placeholder="Email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="text"
                        name="phone"
                        placeholder="Phone"
                        value={formData.phone}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="text"
                        name="address"
                        placeholder="Address"
                        value={formData.address}
                        onChange={handleChange}
                        required
                    />

                    <button type="submit">
                        {editingMemberId ? "Update Member" : "Save Member"}
                    </button>

                    <button
                        type="button"
                        onClick={resetForm}
                    >
                        Cancel
                    </button>

                </form>
            )}

            <table>

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>

                    {isAdmin && <th>Actions</th>}
                </tr>
                </thead>

                <tbody>

                {members.map((member) => (

                    <tr key={member.id}>

                        <td>{member.id}</td>
                        <td>{member.name}</td>
                        <td>{member.email}</td>
                        <td>{member.phone}</td>
                        <td>{member.address}</td>

                        {isAdmin && (
                            <td>

                                <button onClick={() => handleEdit(member)}>
                                    Edit
                                </button>

                                <button onClick={() => handleDelete(member.id)}>
                                    Delete
                                </button>

                            </td>
                        )}

                    </tr>

                ))}

                </tbody>

            </table>

            {members.length === 0 && <p>No members found.</p>}

        </div>
    );
}

export default Members;
