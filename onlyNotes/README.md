📝 OnlyNotes

A JWT-secured note-taking application built with Spring Boot and MongoDB.
OnlyNotes lets users create, update, delete, and organize personal notes — similar to Google Keep but with authentication and role-based access control.

🚀 Features

User Authentication & Authorization with JWT

CRUD Operations on Notes (Create, Read, Update, Delete)

Favorites: Toggle favorite notes & fetch all favorites

Pagination & Sorting for large note collections

DTO-based design (clean separation between entity & exposed API)

Swagger/OpenAPI documentation at /docs

Secure Password Storage with BCrypt

Role-based Access Control (USER, ADMIN)

🛠️ Tech Stack

Backend: Spring Boot (3.x), Spring Security, JWT, Spring Data MongoDB

Database: MongoDB (Atlas/Local)

Build Tool: Maven

Docs: Springdoc OpenAPI (Swagger UI)

Other: Lombok, SLF4J (logging)

📂 Project Structure
onlyNotes/
├── src/main/java/com/example/onlyNotes
│    ├── config/         # Swagger, Security & JWT configs
│    ├── controller/     # REST Controllers
│    ├── dto/            # Data Transfer Objects
│    ├── exception/      # Custom exception handling
│    ├── model/          # Entities (User, Notes)
│    ├── repo/           # Repositories
│    ├── service/        # Business logic
│    └── OnlyNotesApp.java
├── src/main/resources/
│    ├── application.yml
├── pom.xml
└── README.md

🔑 API Endpoints
Auth

POST /public/register → Register user

POST /public/login → Login & get JWT

Notes

POST /notes/create → Create a new note

GET /notes/all → Fetch all notes (paginated)

GET /notes/{id} → Fetch note by ID

PATCH /notes/fav/{id} → Toggle favorite

DELETE /notes/{id} → Delete note

User

GET /user/profile → Get current user profile

DELETE /user/delete → Delete user

🔒 Security

JWT-based authentication (Authorization: Bearer <token>)

Passwords hashed using BCrypt

Role-based access control:

USER: Manage personal notes

ADMIN: Access admin endpoints

📜 Swagger Docs

Once app is running:
👉 http://localhost:8080/docs

🏗️ Getting Started
Prerequisites

Java 17+

MongoDB (local or Atlas)

Maven 3.x

Steps
# Clone repo
git clone https://github.com/your-username/onlyNotes.git
cd onlyNotes

# Build
mvn clean install

# Run
mvn spring-boot:run


⚡Built with ❤️ by Abdul Rahman