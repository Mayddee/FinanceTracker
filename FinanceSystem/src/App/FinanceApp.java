package App;
import java.util.Scanner;

import database.UserRepository;
import managers.UserManager;
import models.User;
import services.AuthService;

import java.time.LocalDate;

public class FinanceApp {
	private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n===== Добро пожаловать в FinanceApp! =====");
        
        seedData();

        while (true) {
            if (AuthService.getCurrentUser() == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    

    private static void seedData() {
    	
    	AuthService.registerUser("Madina", "mad@kbtu.kz", "pass1");
    	 AuthService.logout();
        AuthService.registerUser("Ivan", "ivan@gmail.com", "password2");
        AuthService.logout();
        AuthService.registerUser("Alisa", "alisa@mail.ru", "password3");
        
        AuthService.logout();
    }

    private static void showAuthMenu() {
        
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
        System.out.print("Выберите действие: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                registerUser();
                break;
            case "2":
                loginUser();
                break;
            case "3":
                System.out.println("Выход из программы...");
                System.exit(0);
                break;
            default:
                System.out.println("Ошибка: неверный ввод.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n===== Главное меню =====");
        System.out.println("1. Управление профилем");
        System.out.println("2. Управление транзакциями");
        System.out.println("3. Управление бюджетом");
        System.out.println("4. Управление целями");
        System.out.println("5. Аналитика и отчёты");
        System.out.println("6. Выход из аккаунта");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                manageProfile();
                break;
            case "2":
                manageTransactions();
                break;
            case "3":
                manageBudget();
                break;
            case "4":
                manageGoals();
                break;
            case "5":
                viewAnalytics();
                break;
            case "6":
                AuthService.logout();
                break;
            default:
                System.out.println("Ошибка: неверный ввод.");
        }
    }

    private static void registerUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        
        AuthService.registerUser(name, email, password);
    }

    private static void loginUser() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        
        try {
            AuthService.login(email, password);
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void manageProfile() {
    	System.out.println("\n===== Управление профилем =====");
        System.out.println("1. Редактировать профиль");
        System.out.println("2. Удалить аккаунт");
        System.out.println("3. Назад");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine();
        User currentUser = AuthService.getCurrentUser();
        
        switch (choice) {
            case "1":
                editUserProfile();
                break;
            case "2":
                if (currentUser != null) {
                    currentUser.getUserManager().deleteUser();
                    System.out.println("Ваш аккаунт успешно удален.");
                } else {
                    System.out.println("Ошибка: пользователь не найден.");
                }
                break;
            case "3":
                return;
            default:
                System.out.println("Ошибка: неверный ввод.");
        }
    }

    private static void editUserProfile() {
    	User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }
        System.out.print("Введите новое имя (оставьте пустым, если не менять): ");
        String name = scanner.nextLine();
        System.out.print("Введите новый email (оставьте пустым, если не менять): ");
        String email = scanner.nextLine();
        System.out.print("Введите новый пароль (оставьте пустым, если не менять): ");
        String password = scanner.nextLine();

        currentUser.getUserManager().editUserProfile(name, email, password);
    }

    private static void manageTransactions() {
    	User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }
        
        UserManager userManager = currentUser.getUserManager();        
        while (true) {
            System.out.println("\n===== Управление транзакциями =====");
            System.out.println("1. Добавить транзакцию");
            System.out.println("2. Просмотреть транзакции");
            System.out.println("3. Редактировать транзакцию");
            System.out.println("4. Удалить транзакцию");
            System.out.println("5. Назад");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите сумму: ");
                    double sum = Double.parseDouble(scanner.nextLine());

                    System.out.print("Введите тип (доход/расход): ");
                    String type = scanner.nextLine().toLowerCase();

                    System.out.print("Введите категорию: ");
                    String category = scanner.nextLine();

                    userManager.createTransaction(sum, type, category);
                    break;

                case "2":
                    userManager.getUserTransactions();
                    break;

                case "3":
                    System.out.print("Введите ID транзакции для редактирования: ");
                    int transactionId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Введите новую сумму: ");
                    double newSum = Double.parseDouble(scanner.nextLine());

                    System.out.print("Введите новую категорию: ");
                    String newCategory = scanner.nextLine();

                    try {
                        userManager.editTransaction(transactionId, newSum, newCategory);
                    } catch (Exception e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Введите ID транзакции для удаления: ");
                    int deleteTransactionId = Integer.parseInt(scanner.nextLine());

                    try {
                        userManager.deleteTransaction(deleteTransactionId);
                    } catch (Exception e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Ошибка: неверный ввод.");
            }
        }
    }

    private static void manageBudget() {
    	User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }
        
        UserManager userManager = currentUser.getUserManager();
        
        System.out.println("\n===== Управление бюджетом =====");
        System.out.println("1. Установить месячный бюджет");
        System.out.println("2. Проверить текущий бюджет");
        System.out.print("Выберите действие: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Введите сумму месячного бюджета: ");
                double budget = scanner.nextDouble();
                scanner.nextLine();
                userManager.getCurrentUser().setMonthlyBudget(budget);
                System.out.println("Бюджет установлен: " + budget);
                break;
            case 2:
                double currentBudget = userManager.getCurrentUser().getMonthlyBudget();

            	System.out.println("Текущий месячный бюджет: " + currentBudget);
                break;
            default:
                System.out.println("Неверный ввод.");
        }
    }

    private static void manageGoals() {
    	User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }
        
        UserManager userManager = currentUser.getUserManager();
        
        System.out.println("\n===== Управление целями =====");
        System.out.println("1. Создать новую цель");
        System.out.println("2. Посмотреть текущие цели");
        System.out.print("Выберите действие: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Введите название цели: ");
                String name = scanner.nextLine();
                System.out.print("Введите описание: ");
                String description = scanner.nextLine();
                System.out.print("Введите сумму: ");
                double amount = scanner.nextDouble();
                System.out.print("Введите приоритет (1 - высокий, 5 - низкий): ");
                int priority = scanner.nextInt();
                scanner.nextLine();
                userManager.createGoal(name, description, amount, priority);
                break;
            case 2:
                System.out.println("Текущие цели:");
                userManager.getUserGoals();
                break;
            default:
                System.out.println("Неверный ввод.");
        }
    }

    private static void viewAnalytics() {
    	User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }
        
        UserManager userManager = currentUser.getUserManager();
        System.out.println("\n===== Аналитика и отчёты =====");
        System.out.println("1. Финансовый отчет");
        System.out.println("2. Анализ расходов по категориям");
        System.out.println("3. Подсчет чистого капитала");
        System.out.print("Выберите действие: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                userManager.printFinancialReport();
                break;
            case 2:
                System.out.print("Введите ваш ID: ");
                String userId = scanner.nextLine();
                System.out.println("Анализ расходов по категориям: " + userManager.analyzeExpensesByCategory(userId));
                break;
            case 3:
                userManager.calculateNetWorth();
                break;
            default:
                System.out.println("Неверный ввод.");
        }
    }
    
    //Testing some changes
}
