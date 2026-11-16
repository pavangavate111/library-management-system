// Purpose: Display error messages when an API request fails.


function ErrorMessage({message}) {
    return (
        <p style={{color: "red"}}>
            {message}
        </p>
    );
}

export default ErrorMessage;