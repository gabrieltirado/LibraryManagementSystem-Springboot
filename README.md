# LibraryManagementSystem-Springboot
RESTful API built with Spring Boot for managing books, authors, users, and borrowing operations. Includes full CRUD functionality, borrowing limits, JWT-based authentication, and persistent storage in a relational database.

Documentation: 
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [API Docs](http://localhost:8080/v3/api-docs)

## Features
- **Book Management**: Add, update, delete, and retrieve books.
- **Author Management**: Manage authors and their biographies.
- **User Management**: Register, update, and delete users.
- **Borrowing Management**: Handle book borrowing and returning processes.
- **Authentication**: Secure endpoints with JWT-based authentication.


Class Diagram:
```mermaid
%% Entidad: User
class User {
  +Long id
  +String name
  +String email
  +String libraryId
  +String role
  +List~BorrowingRecord~ borrowingRecords
}

%% Entidad: Author
class Author {
  +Long id
  +String name
  +List~Book~ books
}

%% Entidad: Book
class Book {
  +Long id
  +String title
  +String isbn
  +List~Author~ authors
}

%% Entidad: BorrowingRecord
class BorrowingRecord {
  +Long id
  +User user
  +Book book
  +LocalDate borrowDate
  +LocalDate dueDate
  +LocalDate returnedDate
  +boolean isActive()
}

%% Relaciones
User "1" --> "0..*" BorrowingRecord : has
Book "1" --> "0..*" BorrowingRecord : is borrowed in
Author "1" --> "0..*" Book : writes
Book "1" --> "0..*" Author : written by

```
![Class Diagram](image.png)

Diagram Relational Database:
```mermaid
    USER ||--o{ BORROWING_RECORD : has
    BOOK ||--o{ BORROWING_RECORD : has
    AUTHOR ||--o{ BOOK : writes

    USER {
        int id
        string name
        string email
        string libraryId
        string role
    }

    BOOK {
        int id
        string title
        string isbn
        boolean availability
    }

    AUTHOR {
        int id
        string name
    }

    BORROWING_RECORD {
        int id
        date borrowDate
        date dueDate
        date returnedDate
    }

```

![Entity Relationship Diagram](image-1.png)

