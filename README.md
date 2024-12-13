# Extack (Expense Tracker)

An **Expenses Tracker App** built with **Spring Boot** and **PostgreSQL**, featuring:
- **Authentication** for secure access.
- **Swagger** for comprehensive API documentation.

## Features
- Manage income and expenses.
- User authentication and authorization.
- API documentation available via Swagger.

## Tech Stack
- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **API Documentation**: Swagger

## Getting Started

### Prerequisites
- Java 17 or later
- Maven
- PostgreSQL
- Postman (optional, for testing)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/ahimsarijalu/extrack.git
   cd extrack
   ```

2. Configure the environment variables:
   - Copy `.env.example` to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Update the `.env` file with your configuration:
     ```env
     DATABASE_URL=jdbc:postgresql://localhost:5432/your-database
     DATABASE_USERNAME=your-username
     DATABASE_PASSWORD=your-password
     ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Access the Application
- API documentation: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Test APIs with Postman (collection available).

## Postman Collection
You can find the Postman collection file at `extrack.postman_collection.json` in the root directory. Import it into Postman to easily test the APIs.

## Contributing
Contributions are welcome! Follow these steps:
1. Fork the repository.
2. Create a new branch for your feature/fix.
3. Submit a pull request with detailed information about your changes.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact
For questions or support, feel free to reach out:
- Email: [ahimsarijalu@gmail.com](mailto:ahimsarijalu@gmail.com)
- GitHub: [ahimsarijalu](https://github.com/ahimsarijalu)

