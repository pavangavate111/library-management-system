
import { useEffect, useState } from "react";

import {
    getAllBooks,
    addBook,
    updateBook,
    deleteBook
} from "../services/bookService";

import { useAuth } from "../context/AuthContext";
import "./Books.css";

function Books() {

    const { user } = useAuth();
    const isAdmin = user?.role === "ADMIN";

    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [showForm, setShowForm] = useState(false);

    const [formData, setFormData] = useState({
        isbn: "",
        title: "",
        author: "",
        category: "",
        publisher: "",
        totalCopies: 1,
        availableCopies: 1
    });

    const [editingBookId, setEditingBookId] = useState(null);
    const [searchKeyword, setSearchKeyword] = useState("");

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const resetForm = () => {
        setFormData({
            isbn: "",
            title: "",
            author: "",
            category: "",
            publisher: "",
            totalCopies: 1,
            availableCopies: 1
        });

        setEditingBookId(null);
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

        addBook({
            ...formData,
            totalCopies: Number(formData.totalCopies),
            availableCopies: Number(formData.availableCopies)
        })
            .then(() => {
                alert("Book added successfully!");
                resetForm();
                return getAllBooks(currentPage, 5, "id");
            })
            .then((response) => {
                setBooks(response.data.content);
                setTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error("Add book error:", error);
                alert(
                    error.response?.data?.message ||
                    "Failed to add book. Please check the details."
                );
            });
    };

    const handleEdit = (book) => {

        if (!isAdmin) return;

        setEditingBookId(book.id);

        setFormData({
            isbn: book.isbn,
            title: book.title,
            author: book.author,
            category: book.category || "",
            publisher: book.publisher || "",
            totalCopies: book.totalCopies,
            availableCopies: book.availableCopies,
            version: book.version
        });

        setShowForm(true);
    };

    const handleUpdate = (e) => {

        e.preventDefault();

        if (!isAdmin) return;

        updateBook(editingBookId, {
            ...formData,
            totalCopies: Number(formData.totalCopies),
            availableCopies: Number(formData.availableCopies)
        })
            .then(() => {
                alert("Book updated successfully!");
                resetForm();
                return getAllBooks(currentPage, 5, "id");
            })
            .then((response) => {
                setBooks(response.data.content);
                setTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error("Update book error:", error);
                alert(
                    error.response?.data?.message ||
                    "Failed to update book. Please check the details."
                );
            });
    };

    const handleDelete = (id) => {

        if (!isAdmin) return;

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this book?"
        );

        if (!confirmDelete) return;

        deleteBook(id)
            .then(() => {
                alert("Book deleted successfully!");
                return getAllBooks(currentPage, 5, "id");
            })
            .then((response) => {
                setBooks(response.data.content);
                setTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error("Delete book error:", error);

                const message =
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    "Failed to delete book.";

                alert(message);
            });
    };

    const handleSearch = (e) => {
        setSearchKeyword(e.target.value);
    };

    useEffect(() => {

        setLoading(true);
        setError("");

        getAllBooks(currentPage, 5, "id")
            .then((response) => {
                setBooks(response.data.content);
                setTotalPages(response.data.totalPages);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Load books error:", error);
                setError("Failed to load books.");
                setLoading(false);
            });

    }, [currentPage]);

    if (loading) {
        return <p>Loading books...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    const filteredBooks = books.filter((book) =>
        (book.title || "").toLowerCase().includes(searchKeyword.toLowerCase()) ||
        (book.author || "").toLowerCase().includes(searchKeyword.toLowerCase()) ||
        (book.isbn || "").toLowerCase().includes(searchKeyword.toLowerCase())
    );

    return (

        <div className="books">

            <h1>Manage Books</h1>

            <input
                type="text"
                placeholder="Search books..."
                value={searchKeyword}
                onChange={handleSearch}
            />

            {isAdmin && (
                <button onClick={() => {
                    if (showForm) {
                        resetForm();
                    } else {
                        setShowForm(true);
                    }
                }}>
                    Add New Book
                </button>
            )}

            {isAdmin && showForm && (

                <form onSubmit={editingBookId ? handleUpdate : handleSubmit}>

                    <input
                        type="text"
                        name="isbn"
                        placeholder="ISBN"
                        value={formData.isbn}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="text"
                        name="title"
                        placeholder="Book Title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="text"
                        name="author"
                        placeholder="Author"
                        value={formData.author}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="text"
                        name="category"
                        placeholder="Category"
                        value={formData.category}
                        onChange={handleChange}
                    />

                    <input
                        type="text"
                        name="publisher"
                        placeholder="Publisher"
                        value={formData.publisher}
                        onChange={handleChange}
                    />

                    <input
                        type="number"
                        name="totalCopies"
                        placeholder="Total Copies"
                        min="1"
                        max="10000"
                        value={formData.totalCopies}
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="number"
                        name="availableCopies"
                        placeholder="Available Copies"
                        min="0"
                        max="10000"
                        value={formData.availableCopies}
                        onChange={handleChange}
                        required
                    />

                    <button type="submit">
                        {editingBookId ? "Update Book" : "Save Book"}
                    </button>

                    <button type="button" onClick={resetForm}>
                        Cancel
                    </button>

                </form>
            )}

            <table>

                <thead>
                <tr>
                    <th>ID</th>
                    <th>ISBN</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Category</th>
                    <th>Publisher</th>
                    <th>Total Copies</th>
                    <th>Available Copies</th>

                    {isAdmin && <th>Actions</th>}
                </tr>
                </thead>

                <tbody>

                {filteredBooks.map((book) => (

                    <tr key={book.id}>

                        <td>{book.id}</td>
                        <td>{book.isbn}</td>
                        <td>{book.title}</td>
                        <td>{book.author}</td>
                        <td>{book.category}</td>
                        <td>{book.publisher}</td>
                        <td>{book.totalCopies}</td>
                        <td>{book.availableCopies}</td>

                        {isAdmin && (
                            <td>
                                <button onClick={() => handleEdit(book)}>
                                    Edit
                                </button>

                                <button onClick={() => handleDelete(book.id)}>
                                    Delete
                                </button>
                            </td>
                        )}

                    </tr>

                ))}

                </tbody>

            </table>

            {filteredBooks.length === 0 && <p>No books found.</p>}

            <div className="pagination">

                <button
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage === 0}
                >
                    Previous
                </button>

                <span>
                    Page {currentPage + 1} of {totalPages}
                </span>

                <button
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1 || totalPages === 0}
                >
                    Next
                </button>

            </div>

        </div>
    );
}

export default Books;
