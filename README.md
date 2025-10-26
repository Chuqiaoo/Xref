# 📑 Crossref Integration API

This project implements a **Spring Boot** application that fetches article metadata from the **Crossref API** given a list of DOIs, stores it in a **PostgreSQL** database, and exposes a REST API.

---

## 🚀 Features

- Accepts a JSON array of DOIs (up to 200) via REST API
- Retrieves article metadata from Crossref API:
   - Title
   - Authors and affiliations
   - Published date
   - Review info (if available)
- Persists results in PostgreSQL
- Includes Swagger/OpenAPI UI for easy testing

---

## 🧩 Prerequisites

- Java **17+**
- Maven **3.8+**
- Docker *(optional, for PostgreSQL container)*

---

## ⚙️ Setup Instructions

### 1. Clone the Repository


### 2. Start PostgreSQL (optional, via Docker)
    docker-compose up -d

### 3. Build & Run
**Using Maven**
```
  ./mvnw clean install
  ./mvnw spring-boot:run
```

The application will start at http://localhost:8080.

**Or start Application in InterlliJ**


Swagger UI

Visit: http://localhost:8080/swagger-ui/index.html#


## Notes

The default database credentials are hardcoded for convenience and assessment purposes.