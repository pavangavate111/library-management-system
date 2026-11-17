# Library Management System

A full-stack Library Management System developed using **Java 21, Spring Boot, React, and MySQL**. This application helps manage books, library members, borrowing transactions, returns, and fines through a secure web-based interface.

## Project Overview

The Library Management System simplifies daily library operations. Administrators can manage books and members, while authenticated users can access library features according to their assigned roles.

## Key Features

### 1. Authentication and Security
- User registration and login
- JWT-based authentication
- Spring Security integration
- Role-based authorization (ADMIN and USER)
- Password encryption using BCrypt
- Forgot password functionality
- Email-based password reset using Gmail SMTP

### 2. Book Management
- Add, view, update, and delete books
- Search books
- View available books
- Pagination and sorting
- Book category statistics
- Protection against deleting books with borrowing history

### 3. Member Management
- Add, view, update, and delete members
- Search and manage member records
- Protection against deleting members with borrowing history

### 4. Borrowing Management
- Issue books to members
- Return borrowed books
- View active borrowings
- View borrowing history
- Track borrowing dates and due dates
- Detect overdue books
- Calculate fines

### 5. Additional Features
- Dashboard and library statistics
- RESTful API development
- Swagger API documentation
- Centralized exception handling
- Input validation
- Scheduled overdue-book processing

## Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate ORM
- REST APIs
- JWT (JSON Web Token)
- BCrypt Password Encoder
- Jakarta Bean Validation
- Maven

### Frontend
- React
- JavaScript
- HTML5
- CSS3
- Axios
- React Router DOM
- Vite

### Database
- MySQL

### Email Integration
- JavaMailSender
- Gmail SMTP
- Email-based password reset

### Development and Testing Tools
- IntelliJ IDEA
- Git and GitHub
- Postman
- Swagger UI
- Maven
- npm

## Project Architecture

The application follows a layered backend architecture with a separate React frontend.

```text
Library-Management-System/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/library/lms/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── mapper/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│           └── application.properties
│
├── frontend/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   └── services/
│   └── package.json
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Application Architecture Flow

```text
React Frontend
      |
      | Axios HTTP Requests
      v
Spring Boot REST Controllers
      |
      v
Service Layer
      |
      v
Repository Layer
      |
      v
MySQL Database
```

Spring Security and JWT are used to secure backend API endpoints.

## Database

**Database Name:** `library_management_db`

Main database entities:
- User
- Book
- Member
- Borrowing

JPA and Hibernate are used for database persistence and entity relationships.

## Installation and Setup

### Prerequisites

Install the following:

- JDK 21
- Node.js and npm
- MySQL Server
- IntelliJ IDEA
- Git

### 1. Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
cd library-management-system
```

### 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE library_management_db;
```

Configure your MySQL connection in:

`src/main/resources/application.properties`

Set your database username and password.

Configure the following environment variables for Gmail SMTP:

```text
MAIL_USERNAME
MAIL_PASSWORD
```

Also configure the frontend URL used for password-reset links.

**Important:** Never commit database passwords, Gmail App Passwords, JWT secrets, or other sensitive credentials to GitHub.

### 3. Run the Spring Boot Backend

From the project root directory, open a terminal and run:

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

Backend URL:

`http://localhost:8080`

### 4. Run the React Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite in your browser.

## API Documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Use Swagger UI or Postman to explore and test REST API endpoints.

## Testing and Verification

The application was manually tested to verify its major functionalities.

Testing included:

- User registration and login
- JWT authentication
- Admin and User access
- Book CRUD operations
- Member CRUD operations
- Book issue and return
- Borrowing history
- Fine management
- Forgot password and password reset
- REST API testing using Postman and Swagger UI

### Build Verification

**Backend:**

```powershell
.\mvnw.cmd clean package -DskipTests
```

**Frontend:**

```bash
npm run build
```

Both backend packaging and frontend production builds completed successfully.

## Future Improvements

- Cloud deployment
- Automated test coverage
- Additional reporting and analytics

## Developer

**Pavan Gavate**

- GitHub: https://github.com/pavangavate111
- LinkedIn: https://linkedin.com/in/pavan-a-gavate
- LeetCode: https://leetcode.com/u/pavanleetcode111

**Project Name:** Library Management System  
**Project Type:** Full-Stack Web Application  
**Backend:** Java and Spring Boot  
**Frontend:** React  
**Database:** MySQL
