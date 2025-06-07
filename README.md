# IAM Service

A modern Identity and Access Management (IAM) service built with Spring Boot 3 and Java 24, implementing best practices for authentication and authorization.

## Features

- User registration and authentication
- JWT-based authentication with access and refresh tokens
- Role-based access control
- Password encryption using BCrypt
- Database auditing
- API documentation with OpenAPI 3
- Monitoring with Spring Actuator
- Database migrations with Flyway
- Caching with Caffeine
- Modern Java features (Records, Pattern Matching, etc.)

## Prerequisites

- Java 24
- Maven
- PostgreSQL

## Configuration

The application can be configured through environment variables or the `application.yml` file:

```yaml
# Database
DB_USERNAME: postgres
DB_PASSWORD: postgres

# JWT
JWT_SECRET: your-256-bit-secret-key-here
```

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/iam-service.git
cd iam-service
```

2. Create a PostgreSQL database:
```bash
createdb iam_db
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api/v1`

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/api/v1/api-docs`

## API Endpoints

### Authentication

- POST `/api/v1/auth/register` - Register a new user
- POST `/api/v1/auth/authenticate` - Authenticate user and get tokens

## Security

The service implements the following security measures:

- Password hashing using BCrypt
- JWT-based authentication
- Role-based access control
- CORS configuration
- CSRF protection
- Secure headers
- Rate limiting

## Monitoring

Health and metrics endpoints are available through Spring Actuator:

- Health check: `http://localhost:8080/api/v1/actuator/health`
- Metrics: `http://localhost:8080/api/v1/actuator/metrics`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 