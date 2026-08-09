# 📝 OnlyNotes

A JWT-secured note-taking app built with Spring Boot and MongoDB.  
Create, organise, and favourite your notes — think Google Keep, but with your own backend.

---

## Features

- 🔐 JWT authentication with BCrypt password hashing
- 📒 Full CRUD on notes — create, read, update, delete
- ⭐ Toggle favourite notes and fetch them separately
- 📄 Pagination & sorting for large note collections
- 🧼 DTO-based design — clean separation between entity and API layer
- 📖 Swagger UI auto-docs at `/docs`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.5.5, Spring Security, Spring Data MongoDB |
| Auth | JWT, BCrypt |
| Database | MongoDB Atlas |
| Docs | Springdoc OpenAPI (Swagger UI) |
| Build | Maven 4.0.0 |
| Utilities | Lombok, SLF4J |

---

## API Reference

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/public/signup` | Register a new user |
| `POST` | `/public/login` | Login and receive a JWT |

### Notes
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/notes/create-note` | Create a new note |
| `GET` | `/notes/get-all-notes` | Get all notes (paginated) |
| `GET` | `/notes/read-note/{noteId}` | Get a note by ID |
| `GET` | `/notes/favourite-note` | Get all favourited notes |
| `PUT` | `/notes/update-note/{noteId}` | Update an existing note |
| `PATCH` | `/notes/toggle-fav/{id}` | Toggle favourite on a note |
| `DELETE` | `/notes/delete-note/{id}` | Delete a note |

### User
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/user/profile` | Get current user's profile |
| `PUT` | `/user/update-user` | Update user details |
| `DELETE` | `/user/delete-user` | Delete user account |

---

## Security

All protected routes require a JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

Passwords are hashed with BCrypt and never stored in plain text.

---

## Getting Started

**Prerequisites:** Java 24 · MongoDB Atlas account · Maven 4.0.0

```bash
# Clone
git clone https://github.com/itsAbdul0612/Only-Notes.git
cd onlyNotes

# Build
mvn clean install

# Run
mvn spring-boot:run
```

Once running, Swagger docs are available at:  
**http://localhost:8080/docs**

---

*Built with ❤️ by [Abdul Rahman](https://github.com/itsAbdul0612)*
