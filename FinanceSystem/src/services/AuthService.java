package services;
import java.util.*;

import App.FinanceApp;
import database.UserRepository;
import models.User;

public class AuthService {
	private static User currentUser = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void login(String email, String password) {
        try {
            if (currentUser != null) {
                throw new Exception("Пользователь уже вошел в систему!");
            }
            if (!UserRepository.getInstance().userExists(email)) {
                throw new Exception("Неверный email!");
            }

            User user = UserRepository.getInstance().getUserByEmail(email);
            if (!user.getPassword().equals(password)) {
                throw new Exception("Неверный пароль!");
            }

            // Устанавливаем текущего пользователя
            currentUser = user;

            System.out.println("Вход выполнен! Привет, " + user.getName());
        } catch (Exception e) {
            System.out.println("Ошибка входа: " + e.getMessage());
        }
    }

    public static void logout() {
        if (currentUser != null) {
            currentUser = null;
            System.out.println("Выход из системы успешно выполнен!");
        }
    }
    
    public static void registerUser(String name, String email, String password) {
        try {
            if (UserRepository.getInstance().userExists(email)) {
                throw new Exception("Пользователь с таким email уже существует!");
            }

            User user = new User(name, email, password);
            UserRepository.getInstance().addUser(user);

            System.out.println("Регистрация успешна! Добро пожаловать, " + user.getName());

           
        } catch (Exception e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
        }
    }

  
}