# Authentication Service Test Guide

## Overview
The authentication service has been successfully implemented with the following components:

## Implemented Features

### 1. User Entity
- **Location**: `src/main/java/com/food_delivery_platform/auth_service/entity/User.java`
- **Fields**: id, username, email, passwordHash, role
- **JPA Annotations**: @Entity, @Table, @Id, @GeneratedValue, @Column, @Enumerated
- **Validation**: @NotBlank, @Email, @Size constraints

### 2. Role Enum
- **Location**: `src/main/java/com/food_delivery_platform/auth_service/entity/Role.java`
- **Values**: USER, ADMIN, RESTAURANT_OWNER, DELIVERY_PARTNER

### 3. Repository Layer
- **Location**: `src/main/java/com/food_delivery_platform/auth_service/repository/UserRepository.java`
- **Extends**: JpaRepository<User, Long>
- **Custom Methods**: findByUsername, findByEmail, existsByUsername, existsByEmail

### 4. DTOs
- **UserDTO**: For signup requests with validation
- **LoginRequest**: For login credentials
- **LoginResponse**: Returns JWT token and user info
- **UserResponse**: For profile information (no sensitive data)

### 5. Service Layer
- **JwtService**: Token generation, validation, and extraction
- **AuthService**: Main business logic for signup, login, profile, token validation

### 6. Controller Layer
- **AuthController**: REST endpoints with proper error handling
- **Endpoints**:
  - `POST /api/auth/signup` - User registration
  - `POST /api/auth/login` - User authentication
  - `GET /api/auth/profile` - Get user profile (requires Authorization header)
  - `POST /api/auth/validate` - Token validation

### 7. Security Configuration
- **SecurityConfig**: Spring Security configuration with password encoder
- **Features**: CSRF disabled, stateless sessions, BCrypt password encoding

### 8. Configuration
- **Database**: H2 in-memory database with console access
- **JPA**: Hibernate with auto DDL generation
- **JWT**: Custom secret key with 24-hour expiration

## API Testing

### 1. Signup
```bash
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": "USER"
}
```

### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "testuser",
  "password": "password123"
}
```

### 3. Get Profile
```bash
GET http://localhost:8080/api/auth/profile
Authorization: Bearer <JWT_TOKEN>
```

### 4. Validate Token
```bash
POST http://localhost:8080/api/auth/validate
Authorization: Bearer <JWT_TOKEN>
```

## Database Access
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

## Security Features
- Password hashing with BCrypt
- JWT token-based authentication
- Token expiration (24 hours)
- Input validation
- Proper error handling
- CORS support

## Next Steps
1. Start the application: `mvn spring-boot:run`
2. Test endpoints using Postman or curl
3. Access H2 console to view database
4. Add integration tests
5. Implement role-based authorization
6. Add refresh token functionality
7. Implement password reset feature