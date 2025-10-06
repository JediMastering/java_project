# 🧠 Project Context — ERP Financial SaaS (Spring Boot + React)

## 🎯 Overview

This project is a **Financial Control SaaS for Legal Entities**, built with **Spring Boot** on the backend and **React with MUI** on the frontend.

The system provides authentication, user management, and access control, allowing **multiple users to use the same application**, but **each one having isolated access to their own financial data**.  
The initial focus is on **individual users (PF)**, but the system is architected to support **multiple companies and business accounts (PJ)** in the future.

---

## ⚙️ Main Technologies

- **Backend:** Spring Boot (latest version)
- **Database:** MySQL
- **ORM:** Spring Data JPA
- **Authentication:** JWT (access and refresh tokens)
- **Security:** Spring Security (with role-based access control)
- **Validation:** `@Validated` + Bean Validation (`@NotBlank`, `@Email`, etc.)
- **CORS:** Configured via `WebConfig` implementing `WebMvcConfigurer`

---

## 🧩 Project Structure

Follow this **modular-by-feature structure** strictly:

src/main/java/com/application/erp/
├── user/
│   ├── controller/
│   │   └── UserController.java
│   │
│   ├── domain/
│   │   ├── entity/User.java
│   │   ├── repository/UserRepository.java
│   │   └── service/UserService.java
│   │
│   ├── infrastructure/
│   │   └── jdbc/UserRepositoryImpl.java
│   │
│   ├── job/
│   │   └── UserCleanupJob.java
│   │
│   ├── dto/
│   │   ├── request/
│   │   │   └── UserRequest.java
│   │   └── response/
│   │       └── UserResponse.java
│   │
│   ├── enum/
│   │   └── UserRole.java
│   │
│   ├── exception/
│   │   └── UserNotFoundException.java
│   │
│   ├── utils/
│   │   └── UserPasswordUtils.java
│   │
│   └── validation/
│       ├── UserEmailValidator.java
│       ├── UserPasswordValidator.java
│       └── UserLoginValidator.java
│
└── shared/
    ├── dto/
    │   ├── ErrorResponse.java
    │   └── PaginationDTO.java
    ├── validation/
    │   └── GenericEmailValidator.java
    └── exception/
        ├── BusinessException.java
        └── GlobalExceptionHandler.java



Each **module** follows the same internal structure (`controller`, `domain`, `infrastructure`, `dto`, `validation`, etc.), ensuring modularity, isolation, and scalability.

---

## 📐 Standards and Best Practices

### 🧱 Architecture and Structure
- Follow a **simplified DDD** approach, keeping `domain` as the core business logic layer.
- The `controller` layer **must not contain business logic** — only handle requests, responses, and service calls.
- The `infrastructure` layer should contain specific implementations (e.g., JDBC, external APIs, scheduled jobs).
- All DTOs must be placed under `dto/request` and `dto/response`.

### 🧠 Clean Code
- Name classes, methods, and variables **clearly and expressively**, without abbreviations.
- Use **constructor-based dependency injection** (avoid field injection with `@Autowired`).
- Avoid code duplication — centralize common validations and utilities in the `shared` module.
- Follow **SOLID** principles, especially **SRP (Single Responsibility Principle)**.

### 🧾 Validation and Exceptions
- Use **Bean Validation** for simple field validations.
- Module-specific validations must be placed under the respective `validation/` package.
- Custom exceptions should extend from `BusinessException`.
- `GlobalExceptionHandler` should handle all exceptions and return standardized responses (`ErrorResponse`).

### 🔒 Security
- Implement **JWT authentication** following current best practices.
- Every protected route must validate the JWT token before granting access.
- Roles and permissions are defined in `UserRole.java`.

### 🧰 Utilities
- Utility and helper methods belong inside the `utils/` folder of each module.
- Generic utilities that are shared across modules belong in `shared/utils/`.

### 🕐 Scheduled Jobs
- Recurring or background jobs should be implemented under the `job/` package (e.g., cleanup tasks, notifications, etc.).

---

## 🧩 Naming Conventions
| Type | Convention |
|------|-------------|
| Controller | `NameController` |
| Service | `NameService` |
| Repository | `NameRepository` |
| DTO | `NameRequest`, `NameResponse` |
| Exception | `NameException` |
| Enum | `NameEnum` |
| Job | `NameJob` |
| Validator | `NameValidator` |
| Utils | `NameUtils` |

---

## 🧠 Guidelines for Gemini

When generating or modifying backend code for this project, **always follow these rules**:

1. Preserve the project structure and folder naming conventions shown above.  
2. Use **modern Spring Boot practices** — avoid legacy or deprecated code.  
3. Prioritize **clean code, readability, and SOLID principles**.  
4. Create **cohesive, decoupled, and testable** classes and methods.  
5. Avoid redundant comments — the code should be self-explanatory.  
6. Always include **annotations, imports, and realistic class names** in examples.  
7. Ensure every new class follows the **modular pattern** of the project.  
8. Validate inputs using `@Validated` and module-specific `Validator` classes.  
9. Return consistent responses using DTOs and centralized error handling.  
10. Use **BeanUtils** when copying properties between entities and DTOs.

---

## 🚀 Short-Term Goals

- Finalize the **User module** with complete authentication and refresh token flow.  
- Create the **Financial Transactions module**, following the same modular structure.

---

## 📈 Future Plans

- Multi-company support (each company with its own users and data).  
- Financial dashboards and reports.  
- Subscription plans (integration with a billing system).  
- Detailed audit and access logs.

---

> **Summary:**  
> This document provides context for Gemini to understand the purpose, architecture, and best practices of the ERP Financial SaaS.  
> Always follow this structure, adhere to clean design principles, and prioritize code quality and scalability.
