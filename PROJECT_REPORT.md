# BudgetBrain: Personal Finance Management Web Application

## 1. Project Title

**BudgetBrain: Personal Finance Management Web Application**

## 2. Team Members

- Student 1: Nima Sherpa 
- Student 2: Sunil Lama 
- Student 3: Suvakar Dahal 

## 3. Project Overview

BudgetBrain is a web application designed to help users manage their personal finances in a simple and organized way. Many students and young adults have difficulty tracking income, expenses, receipts, budgets, and savings goals because this information is often scattered or recorded manually. BudgetBrain solves this problem by providing one finance dashboard where users can record transactions, upload receipt images, create budgets, track savings goals, and search their financial records.

The application was developed as a final web application project using Spring Boot, JPA, PostgreSQL, Thymeleaf, HTML, CSS, and Render deployment. It includes user authentication, database storage, CRUD functions, image upload, keyword search, responsive web design, GitHub source code management, and Render deployment.

## 4. Main Features

### 4.1 User Authentication

Users can create an account, login, and logout securely. Spring Security is used to protect private pages such as the dashboard, finance records, budgets, and savings goals. Passwords are stored using BCrypt password encryption.

### 4.2 Dashboard

After login, users can view a finance dashboard. The dashboard shows total income, total expenses, current balance, total saved amount, recent records, and savings goal progress.

### 4.3 Finance Record CRUD

Users can create, read, update, and delete finance records. Each record includes title, type, category, amount, date, note, and optional receipt image. The record type can be income or expense.

### 4.4 Receipt Image Upload

Users can upload receipt images when creating or editing finance records. Uploaded images are saved in the upload folder and displayed in the records list and detail page.

### 4.5 Budget Management CRUD

Users can create monthly budgets for categories such as food, transport, study, shopping, bills, and other expenses. Each budget includes category, month, limit amount, and note.

### 4.6 Savings Goal CRUD

Users can create and manage savings goals. Each goal includes goal name, target amount, saved amount, deadline, and note. The app calculates and displays progress percentage.

### 4.7 Search Function

Users can search finance records by title, category, or note. Users can also search budgets and savings goals by keyword.

### 4.8 Web Design

The application uses a modern dark finance dashboard design with green, blue, and purple gradient highlights. The UI is responsive and works on desktop and mobile screens.

## 5. Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Main programming language |
| Spring Boot | Backend web application framework |
| Spring Security | Authentication and protected pages |
| Spring Data JPA | Database ORM and repositories |
| PostgreSQL | Relational database |
| Thymeleaf | Server-side HTML templates |
| HTML/CSS | User interface and responsive design |
| Docker | Containerized deployment |
| GitHub | Source code hosting |
| Render | Web application and database deployment |

## 6. Database Design

The database contains four main tables.

### 6.1 app_users

Stores user account information.

| Field | Description |
|---|---|
| id | Primary key |
| username | Unique username |
| password | Encrypted password |
| full_name | User full name |
| email | Unique email |
| role | User role |

### 6.2 finance_records

Stores income and expense records.

| Field | Description |
|---|---|
| id | Primary key |
| title | Record title |
| type | INCOME or EXPENSE |
| category | Finance category |
| amount | Money amount |
| record_date | Transaction date |
| note | Extra description |
| receipt_image_path | Uploaded receipt image path |
| created_at | Created timestamp |
| user_id | Owner user foreign key |

### 6.3 budgets

Stores monthly budget plans.

| Field | Description |
|---|---|
| id | Primary key |
| category | Budget category |
| month | Budget month |
| limit_amount | Budget limit |
| note | Extra note |
| created_at | Created timestamp |
| user_id | Owner user foreign key |

### 6.4 savings_goals

Stores savings goal information.

| Field | Description |
|---|---|
| id | Primary key |
| name | Goal name |
| target_amount | Target money amount |
| saved_amount | Current saved money |
| deadline | Goal deadline |
| note | Extra note |
| created_at | Created timestamp |
| user_id | Owner user foreign key |



## 8. Development Process

First, we analyzed the final project requirements and selected a project topic that could include all required features naturally. We chose BudgetBrain because finance management includes authentication, database records, CRUD, search, and image uploads for receipts.

Second, we designed the database structure. We created entities for users, finance records, budgets, and savings goals. Each finance record, budget, and goal belongs to one user.

Third, we implemented user authentication using Spring Security. We created register and login pages, encrypted passwords, and protected private pages so that only logged-in users can access their own data.

Fourth, we developed CRUD functions for finance records, budgets, and savings goals. We also added image upload for receipt images and keyword search for finance data.

Finally, we improved the web design using responsive HTML and CSS. We prepared the project for GitHub and Render deployment using a Dockerfile and environment variables.

## 9. Problems and Solutions

| Problem | Solution |
|---|---|
| Users need secure login | Used Spring Security and BCrypt password encoding |
| Data must be stored permanently | Used PostgreSQL database with JPA entities |
| Users need to manage records | Implemented CRUD for records, budgets, and goals |
| Users need receipt proof | Added image upload and display feature |
| Users may have many records | Added keyword search function |
| App needs to work on mobile | Designed responsive CSS layout |
| Deployment needs database configuration | Used environment variables for PostgreSQL on Render |

## 10. Conclusion

BudgetBrain successfully meets the final project requirements by providing a complete personal finance management web application. It includes user authentication, PostgreSQL database, JPA, CRUD functions, receipt image upload, search, responsive web design, GitHub source code preparation, and Render deployment support. Through this project, our team practiced backend development, database design, security, file upload, frontend design, and deployment. BudgetBrain can be improved in the future by adding OCR receipt scanning, charts, monthly reports, and bank transaction import.
