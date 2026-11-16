// Create a Reusable Page Heading Component
//Purpose: Reuse the same heading structure across Books, Members, and Borrowings pages.


function PageHeader({ title, description }) {
    return (
        <div>
            <h1>{title}</h1>
            <p>{description}</p>
        </div>
    );
}

export default PageHeader;