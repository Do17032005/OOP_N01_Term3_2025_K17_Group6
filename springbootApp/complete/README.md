# Cinema Booking System - Spring Boot Application

## Overview

This is a comprehensive cinema booking system built with Spring Boot, following Object-Oriented Programming (OOP) best practices and layered architecture patterns. The system allows users to browse movies, book tickets, and manage cinema operations.

## Architecture

The application follows a layered architecture pattern with clear separation of concerns:

```
src/main/java/com/example/servingwebcontent/
├── model/           # Domain entities (JPA entities)
├── repository/      # Data access layer (Spring Data JPA repositories)
├── service/         # Business logic layer
├── controller/      # Web layer (REST controllers)
└── ServingWebContentApplication.java  # Main application class
```

### Layer Responsibilities

1. **Model Layer (Domain Entities)**
   - Contains JPA entities representing business objects
   - Implements encapsulation with private fields and public getters/setters
   - Includes business logic methods related to the entity itself
   - Uses proper JPA annotations for database mapping

2. **Repository Layer (Data Access)**
   - Spring Data JPA repositories for database operations
   - Custom query methods for complex data retrieval
   - No business logic, only data access operations

3. **Service Layer (Business Logic)**
   - Contains all business logic and validation rules
   - Orchestrates operations between multiple repositories
   - Implements transaction management
   - Handles business exceptions and validation

4. **Controller Layer (Web Layer)**
   - Handles HTTP requests and responses
   - Delegates business logic to service layer
   - Manages view rendering and redirects
   - Handles user input validation and error responses

## OOP Best Practices Implemented

### 1. Encapsulation
- All entity fields are private with public getters/setters
- Business logic methods are encapsulated within entities
- Service classes encapsulate business operations

### 2. Inheritance
- Used appropriately for "is-a" relationships
- User entity with role-based inheritance (Customer, Admin)

### 3. Polymorphism
- Repository interfaces allow for different implementations
- Service interfaces enable easy testing and extension
- Controller interfaces for different types of endpoints

### 4. Abstraction
- Repository interfaces abstract data access details
- Service interfaces abstract business logic
- Clear separation between layers

## Key Features

### 1. Movie Management
- CRUD operations for movies
- Search by title, genre, duration
- Age rating validation
- Release date tracking

### 2. Showtime Management
- Schedule movie showtimes
- Time conflict detection
- Price management
- Availability tracking

### 3. Booking System
- Ticket booking with seat selection
- Booking confirmation and cancellation
- Revenue calculation
- Booking history tracking

### 4. User Management
- User registration and authentication
- Role-based access control (Customer/Admin)
- User profile management

### 5. Room and Seat Management
- Room capacity management
- Seat type classification (Standard, Premium, VIP)
- Seat availability tracking

## Database Design

The system uses MySQL with JPA/Hibernate for object-relational mapping. Key tables include:

- `users` - User accounts and authentication
- `movies` - Movie information and metadata
- `rooms` - Cinema room configuration
- `seats` - Individual seat information
- `showtimes` - Movie screening schedules
- `tickets` - Individual ticket records
- `bookings` - Booking transactions

## Technology Stack

- **Framework**: Spring Boot 3.3.0
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17

## Configuration

### Database Configuration
Update `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cinema_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### JPA Configuration
- `spring.jpa.hibernate.ddl-auto=update` - Automatically update schema
- `spring.jpa.show-sql=true` - Show SQL queries in logs
- Connection pooling with HikariCP

## Running the Application

1. **Prerequisites**
   - Java 17 or higher
   - MySQL 8.0 or higher
   - Maven 3.6 or higher

2. **Setup Database**
   ```sql
   CREATE DATABASE cinema_db;
   ```

3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access Application**
   - URL: http://localhost:8080
   - Default admin credentials: admin/admin123

## API Endpoints

### Movies
- `GET /movies` - List all movies
- `GET /movies/add` - Show add movie form
- `POST /movies/add` - Create new movie
- `GET /movies/edit/{id}` - Show edit movie form
- `POST /movies/edit` - Update movie
- `GET /movies/delete/{id}` - Delete movie
- `GET /movies/search` - Search movies
- `GET /movies/genre/{genre}` - Filter by genre

### Showtimes
- `GET /showtimes` - List all showtimes
- `GET /showtimes/add` - Show add showtime form
- `POST /showtimes/add` - Create new showtime
- `GET /showtimes/available` - List available showtimes

### Bookings
- `GET /bookings` - List all bookings
- `POST /bookings` - Create new booking
- `GET /bookings/{id}` - Get booking details
- `POST /bookings/{id}/cancel` - Cancel booking

## Security Features

- Spring Security integration
- Role-based access control
- Session management
- CSRF protection
- Input validation and sanitization

## Error Handling

- Comprehensive exception handling
- User-friendly error messages
- Logging for debugging
- Validation error display

## Testing

The application includes unit tests for:
- Service layer business logic
- Repository data access
- Controller request handling
- Entity validation

Run tests with:
```bash
mvn test
```

## Future Enhancements

1. **Payment Integration**
   - Online payment processing
   - Multiple payment methods

2. **Advanced Features**
   - Movie reviews and ratings
   - Loyalty program
   - Email notifications
   - Mobile app API

3. **Performance Optimization**
   - Caching implementation
   - Database query optimization
   - Load balancing

## Contributing

1. Follow the established architecture patterns
2. Implement proper error handling
3. Add unit tests for new features
4. Follow Java coding conventions
5. Update documentation as needed

## License

This project is licensed under the MIT License. 