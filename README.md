# Finance Dashboard API

## About This Project

The Finance Dashboard is a backend REST API designed for managing users, financial records, and providing comprehensive dashboard summaries. Built with Spring Boot and Java 17, it enforces simple, custom role-based access management (`ADMIN`, `ANALYST`, `VIEWER`) via request headers.

This application is configured for **Cloud Deployment** using Docker on **Render**, connected to a cloud-based **Aiven MySQL Database**.

### 🏗️ Architecture & Separation of Concerns
This project strictly follows a **Client-Server Architecture**:
- **The Brain (Java Backend):** All business logic, database queries, mathematical calculations (for the dashboard), and security rules (Roles) live exclusively in the Java Spring Boot code.
- **The Remote (HTML Frontend):** The `index.html` file serves purely as an API testing interface. It contains no core logic or power of its own; it simply sends HTTP requests to the Java backend and displays the responses.

### 🛠️ Tech Stack
- **Backend Framework:** Java 17, Spring Boot 3
- **Data Access:** Spring Data JPA, Hibernate
- **Database:** MySQL (Cloud Hosted via Aiven)
- **Containerization:** Docker (Multi-stage Maven build)
- **Frontend / Testing:** Custom Vanilla HTML/CSS/JS API Explorer
- **Validation:** Spring Boot Starter Validation
- **Deployment & Hosting:** Render
- **Build Tool:** Maven

---

## 🚀 Live Testing & API Explorer
This project features a specially built **Frontend Dashboard** to let you explore and test the APIs natively in your browser!

1. Open the application URL (or `http://localhost:8080` if running locally).
2. You will be greeted by a gorgeous dark-mode API Explorer.
3. Automatically send requests and manipulate headers directly from the UI dropdowns.
4. Experiment with Role-Based Access Control by seeing what `ADMIN`, `ANALYST`, and `VIEWER` roles are permitted to do!

---

## 🔐 Role-Based Access Control

The API requires an `X-User-Role` header on almost all endpoints. 

| Role | Summary Permissions |
| --- | --- |
| **VIEWER** | Strictly read-only for explicit records. Cannot read master lists, Cannot create data, Cannot view analytical dashboards. |
| **ANALYST**| Operations role. Can view analytical dashboards, read master lists, and create/update financial records. Cannot modify users. |
| **ADMIN** | God-mode. Can do everything Analysts can do, plus delete records and fully manage (create/delete) users. |

---

## 💻 How to Run Locally

If you don't wish to test the live Render deployment, you can run it locally:

1. **Requirements**: Java 17, Maven, and MySQL.
2. **Database Setup**: Create a local database named `finance_dashboard`.
3. **Environment Setup**: The application uses environment variables for security. You must pass them:
   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/finance_dashboard?createDatabaseIfNotExist=true&useSSL=false"
   export DB_USER="root"
   export DB_PASS="your_password"
   ```
4. **Run**:
   ```bash
   mvn clean spring-boot:run
   ```

---

## 📡 Available API Endpoints

### Health / UI
- `GET /` - Fetches the HTML API Explorer Dashboard
- `GET /api/test` - Health check

### Dashboard Endpoints
- `GET /api/dashboard/summary?userId={id}` - Get full analytics and math summaries.

### Financial Records Endpoints
- `POST /api/records` - Create a Record
- `GET /api/records` - Get All Records
- `GET /api/records/{id}` - Get Record Details
- `PUT /api/records/{id}` - Update a Record
- `DELETE /api/records/{id}` - Delete a Record

### Users Endpoints
- `POST /api/users` - Create a New User
- `GET /api/users` - Get All Users
- `GET /api/users/{id}` - Get User Details
- `PATCH /api/users/{id}/role?newRole={role}` - Update User Role
- `PATCH /api/users/{id}/status?newStatus={status}` - Update User Status
- `DELETE /api/users/{id}` - Delete a User

Live :- https://finance-dashboard-9xom.onrender.com/
