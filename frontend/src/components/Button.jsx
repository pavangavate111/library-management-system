// Button.jsx is a React component file used to create a reusable button.
//Button.jsx is a React component file.
import "./Button.css";

function Button({ children, onClick }) {
    return (
        <button onClick={onClick}>
            {children}
        </button>
    );
}

export default Button;