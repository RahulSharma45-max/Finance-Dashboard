# Finance Dashboard API

## About This Project

The Finance Dashboard is a backend REST API designed for managing users, financial records, and providing comprehensive dashboard summaries. Built with Spring Boot and Java 17, it uses MySQL for reliable data storage and enforces simple, custom role-based access management via request headers.

## How to Run Locally

1. **Requirements**: Make sure you have Java 17, Maven, and MySQL installed locally.
2. **Database Setup**: Log into your local MySQL server and create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS finance_dashboard;
   ```
3. **Environment Setup (Optional)**:
   The application uses environment variables. However, it will gracefully fallback to default local credentials (defined in `application.properties`) automatically. To explicitly pass them:
   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/finance_dashboard?createDatabaseIfNotExist=true&useSSL=false"
   export DB_USER="root"
   export DB_PASS="your_password"
   ```
4. **Run the Application**:
   From inside the `finance-dashboard` project folder
   execute:
   ```bash
   mvn clean spring-boot:run
   ```
   The server will start successfully on `http://localhost:8080`.

## How to Run APIs

You can use **Postman** to send requests to the API endpoints.

> **Important**: Ensure you pass the header `X-User-Role` on your requests. Many endpoints require this check to authorize access.

### Dashboard Endpoints

- **Get Dashboard Summary**
  - `GET /api/dashboard/summary?userId={id}`

### Financial Records Endpoints

- **Create a Record**
  - `POST /api/records`
- **Get All Records (by user or all if admin)**
  - `GET /api/records`
- **Get Record Details**
  - `GET /api/records/{id}`
- **Update a Record**
  - `PUT /api/records/{id}`
- **Delete a Record**
  - `DELETE /api/records/{id}`

### Users Endpoints

- **Create a New User**
  - `POST /api/users`
- **Get All Users**
  - `GET /api/users`
- **Get User Details**
  - `GET /api/users/{id}`
- **Update User Role**
  - `PATCH /api/users/{id}/role?newRole={role}`
- **Update User Status**
  - `PATCH /api/users/{id}/status?newStatus={status}`
- **Delete a User**
  - `DELETE /api/users/{id}`
