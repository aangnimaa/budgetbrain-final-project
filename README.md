# BudgetBrain - Final Project

BudgetBrain is a personal finance management web application built for the final project requirements. It allows users to register, login, manage income and expense records, upload receipt images, create monthly budgets, track savings goals, and search finance data.

## Required Features Covered

| Requirement | BudgetBrain Implementation |
|---|---|
| User Authentication | Sign up, login, logout using Spring Security |
| Database | PostgreSQL |
| JPA | Spring Data JPA entities and repositories |
| CRUD | Finance records, budgets, and savings goals |
| Image Upload | Upload and display receipt images |
| Search | Search records, budgets, and goals by keyword |
| Web Design | Responsive dark finance dashboard UI |
| Deployment | Dockerfile included for Render deployment |

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- HTML / CSS
- Docker
- Render

## Main Pages

- `/` - Landing page
- `/register` - Sign up
- `/login` - Login
- `/dashboard` - Finance summary dashboard
- `/records` - Income/expense CRUD, receipt image upload, search
- `/budgets` - Monthly budget CRUD and search
- `/goals` - Savings goal CRUD and search

## How to Run Locally

### 1. Create PostgreSQL database

Create a database named:

```sql
CREATE DATABASE budgetbrain;
```

### 2. Update database settings

The app reads database settings from environment variables. For local testing, the default values are:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/budgetbrain
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
UPLOAD_DIR=uploads
```

If your PostgreSQL password is different, update `src/main/resources/application.properties` or set environment variables.

### 3. Run the app

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

## Demo Flow

1. Create a new account.
2. Login.
3. Add an income record.
4. Add an expense record with a receipt image.
5. Search records by keyword.
6. Create a monthly budget.
7. Create a savings goal.
8. Edit and delete records to show CRUD.
9. Logout.

## Render Deployment

### Environment Variables

Add these variables in Render:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_RENDER_INTERNAL_HOST:5432/YOUR_DATABASE_NAME
SPRING_DATASOURCE_USERNAME=YOUR_DATABASE_USER
SPRING_DATASOURCE_PASSWORD=YOUR_DATABASE_PASSWORD
UPLOAD_DIR=/app/uploads
```

### Deployment Steps

1. Push this project to GitHub.
2. Create a PostgreSQL database on Render.
3. Create a new Render Web Service from your GitHub repository.
4. Select Docker deployment.
5. Add the environment variables above.
6. Deploy the app.

## Notes

- Uploaded images are saved to the upload directory.
- On free Render services, uploaded files may not be permanent after redeploy unless persistent storage is configured. For a class demo, this is acceptable, but for production, use cloud storage.
