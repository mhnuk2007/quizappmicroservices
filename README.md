# 🧠 QuizApp Microservices

A full-featured **microservices-based Quiz Application** built using **Spring Boot**, **Docker**, and **Eureka Service Discovery**, with **JWT authentication** and API Gateway routing.

---

## 🏗️ Architecture Overview

```
                          +---------------------+
                          |   Service Registry  |
                          | (Eureka Server)     |
                          +----------+----------+
                                     |
      +----------------------+       |       +------------------------+
      |   API Gateway        |-------+-------|    Auth Service        |
      | (Spring Cloud)       |               |  (JWT Authentication)  |
      +----------+-----------+               +------------------------+
                 |
      +----------+------------+
      |                       |
+-----+-----+           +------+------+
| Quiz Service|<------->|Question Svc |
|-------------|         |-------------|
| Creates,    |         | Manages     |
| evaluates   |         | quiz Qs     |
+-------------+         +-------------+
```

---

## ⚙️ Microservices

| Service Name | Port | Description |
|---------------|------|-------------|
| **service-registry** | `8761` | Eureka Service Discovery Server |
| **api-gateway** | `8080` | Gateway routing for all microservices |
| **auth-service** | `8083` | Handles user registration, login, and JWT generation |
| **quiz-service** | `8082` | Manages quizzes, communicates with question-service |
| **question-service** | `8081` | CRUD operations for quiz questions |

---

## 🐳 Docker Setup

### Build All Services

Each service contains its own `Dockerfile`. You can either build individually or with Docker Compose.

**Option 1: Build manually**
```bash
mvn clean package -DskipTests
docker build -t auth-service ./auth-service
docker build -t question-service ./question-service
docker build -t quiz-service ./quiz-service
docker build -t api-gateway ./api-gateway
docker build -t service-registry ./service-registry
```

**Option 2: Build & Run with Docker Compose**
```bash
docker-compose up --build
```

---

## 🧾 Environment Variables

| Variable | Service | Description |
|-----------|----------|-------------|
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | All | Eureka registration URL |
| `JWT_SECRET` | Auth Service | Secret key for token signing |
| `DB_URL`, `DB_USER`, `DB_PASS` | All | Database credentials if using MySQL/Postgres |

---

## 🔑 Authentication Flow

1. **User registers or logs in** → Auth Service issues a JWT.
2. **JWT is sent in Authorization header** (`Bearer <token>`) with every request.
3. **API Gateway** validates token and forwards requests to other services.
4. Services communicate securely via Feign Clients.

---

## 🚀 Access Points

| Service | Example Endpoint                         |
|----------|------------------------------------------|
| Auth Service | `POST /auth/signup`, `POST /auth/signin` |
| Quiz Service | `GET /quiz/get/{id}`                     |
| Question Service | `GET /question/all`                      |
| Gateway | `http://localhost:8080/...`              |
| Eureka Dashboard | `http://localhost:8761`                  |

---

## 🧠 Technologies Used

- **Spring Boot 3+**
- **Spring Cloud Netflix Eureka**
- **Spring Cloud Gateway**
- **Spring Security + JWT**
- **Feign Clients**
- **Maven**
- **Docker & Docker Compose**
- **MySQL / PostgreSQL (optional)**

---

## 📜 License

This project is released under the **MIT License**.

---

## 👨‍💻 Author

**Mohan Lal (mhnuk2007)**  
🚀 Passionate Full-Stack Java Developer  
📚 GitHub: [https://github.com/mhnuk2007](https://github.com/mhnuk2007)
