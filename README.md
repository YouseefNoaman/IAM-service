# IAM Service

A modern Identity and Access Management (IAM) service built with Spring Boot 3 and WebFlux,
implementing reactive programming patterns for high performance and scalability.

## Features

- Fully reactive implementation using Spring WebFlux
- User registration and authentication
- JWT-based authentication with access and refresh tokens
- Role-based access control (RBAC)
- Password encryption using BCrypt
- Reactive database access with R2DBC
- Reactive Redis caching
- API documentation with OpenAPI 3
- Health monitoring with Spring Actuator
- Docker support with docker-compose

## Tech Stack

- Java 21
- Spring Boot 3.2.3
- Spring WebFlux
- Spring Security
- R2DBC with PostgreSQL
- Redis for caching
- JWT for authentication
- OpenAPI/Swagger for documentation
- Docker & Docker Compose
- Maven for build management

## Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 16+
- Redis 7+
- Docker & Docker Compose (optional)

## Getting Started

### Local Development

1. Clone the repository:

```bash
git clone <repository-url>
cd iam-service
```

2. Configure environment variables (or update application.yml):

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export REDIS_HOST=localhost
export JWT_SECRET=your-256-bit-secret-key
```

3. Start PostgreSQL and Redis (if using Docker):

```bash
docker-compose up -d db redis
```

4. Run the application:

```bash
./mvnw spring-boot:run
```

### Docker Deployment

To run the entire application stack using Docker:

```bash
docker-compose up -d
```

This will start:

- The IAM service
- PostgreSQL database
- Redis cache

## API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/api-docs

## Authentication Endpoints

### Register a new user

```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```

### Authenticate

```http
POST /api/v1/auth/authenticate
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

Both endpoints return JWT tokens:

```json
{
    "accessToken": "eyJhbGciOiJ...",
    "refreshToken": "eyJhbGciOiJ..."
}
```

## Security

- All passwords are hashed using BCrypt
- JWT tokens are signed with a secure key
- Role-based access control is implemented
- Reactive security context is maintained throughout the request chain

## Health Check

The application's health can be monitored at:

```http
GET /api/v1/health
```

## Configuration

Key configuration properties in `application.yml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/iam_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}

security:
  jwt:
    secret-key: ${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
    access-token:
      expiration: 3600 # 1 hour
    refresh-token:
      expiration: 604800 # 7 days
```

## Building

```bash
./mvnw clean package
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 