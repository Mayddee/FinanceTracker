# FinanceTracker System

## Overview
FinanceTracker is a console-based multi-user financial management application written in Java. It allows users to register, manage personal finances, track expenses and income, set long-term financial goals, and analyze financial performance.

## Features
### User Management
- Register new users with a unique email and password
- Login with email and password verification
- Edit user profile (name, email, password)
- Delete user account

### Financial Management (CRUD Operations)
- Create a transaction (income/expense) with amount, category, date, and description
- Edit transactions (update amount, category, description)
- Delete transactions
- View transactions with filtering by date, category, or type (income/expense)

### Budget Management
- Set a monthly budget
- Track budget exceedance and notify users

### Goal Management
- Set financial saving goals
- Track progress toward goals

### Statistics & Analytics
- Calculate the current balance
- Summarize income and expenses over a specified period
- Analyze expenses by category
- Generate financial reports

### Notifications
- API for spending limit reminders (Planned Feature)
- Email notification integration (Planned Feature)

### Administration
- Admins can view users and their transactions
- Admins can block or delete users

## Technical Requirements
- The application is written in Java
- It is a console-based application
- Uses only Java Core without external libraries (no Spring, etc.)
- Data is stored in memory using Java Collections
- Implements CRUD operations for users and transactions
- Includes user authentication and authorization
- Unit tests cover at least 75% of the code using JUnit5, Mockito, and AssertJ

## Prerequisites
- Java Development Kit (JDK) 17 or later
- Eclipse IDE (or any Java-compatible IDE)

## Installation
1. Clone or download the project:
   ```sh
   git clone https://github.com/your-username/FinanceTracker.git
   ```
2. Open Eclipse IDE.
3. Select **File** > **Import** > **Existing Projects into Workspace**.
4. Choose the root directory of the project and click **Finish**.
5. Ensure the Java compiler compliance level is set correctly in **Project Properties > Java Compiler**.
6. Run the `FinanceApp.java` file.

## Project Structure
```
FinanceSystem/
│── src/
│   ├── App/
│   │   ├── FinanceApp.java
│   │   ├── FinanceAppTest.java
│   ├── database/
│   │   ├── GoalRepository.java
│   │   ├── TransactionRepository.java
│   │   ├── UserRepository.java
│   ├── managers/
│   │   ├── UserManager.java
│   │   ├── UserManagerTest.java
│   ├── models/
│   ├── services/
│   │   ├── AdminService.java
│   │   ├── AuthService.java
│   │   ├── AuthServiceTest.java
│   │   ├── NotificationService.java
│   ├── supports/
│   │   ├── GoalPriorityComparator.java
│   │   ├── Observer.java
│   │   ├── TransactionCategoryComparator.java
│   │   ├── TransactionType.java
│   ├── test/java/services/
│── README.md
```

## Usage
When the program starts, users can interact with the menu to register, log in, and manage their finances.

### Example Menu:
```sh
Welcome to FinanceTracker!
1. Register
2. Login
3. Add Transaction
4. View Transactions
5. Set Budget
6. Track Goals
7. Generate Report
8. Exit
Enter your choice:
```

## Contributing
Contributions are welcome! Fork the repository and submit pull requests to improve the project.

## License
This project is open-source and available for use under a free software license.

## Contact
For questions or suggestions, feel free to open an issue on GitHub.

---
Give the project a ⭐ if you find it useful!
