package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import database.TransactionRepository;
import database.UserRepository;
import models.Transaction;
import models.User;
public class AdminService {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepository.getInstance();

    // Отображение всех пользователей и их транзакций
    public void displayAllUsersWithTransactions() {
        Map<String, User> users = userRepository.getAllUsers();
        Map<String, List<Transaction>> transactions = transactionRepository.getTransactions();

        if (users.isEmpty()) {
            System.out.println("Нет зарегистрированных пользователей.");
            return;
        }

        for (Map.Entry<String, User> entry : users.entrySet()) {
            String userId = entry.getKey();
            User user = entry.getValue();

            System.out.println("Пользователь: " + user.getName() + " (ID: " + userId + ")");
            System.out.println("Текущий баланс: " + user.getBalance() + " KZT");
            System.out.println("Транзакции:");

            List<Transaction> userTransactions = transactions.getOrDefault(userId, new ArrayList<>());
            if (userTransactions.isEmpty()) {
                System.out.println("  - Нет транзакций.");
            } else {
                userTransactions.forEach(t -> System.out.println("  - " + t));
            }
            System.out.println("-------------------------------");
        }
    }

    // Метод для блокировки пользователя
    public void blockUser(String userId) {
        User user = userRepository.getAllUsers().get(userId);
        if (user != null) {
            user.setBlocked(true);
            System.out.println("Пользователь " + user.getName() + " заблокирован.");
        } else {
            System.out.println("Пользователь с ID " + userId + " не найден.");
        }
    }

    // Метод для удаления пользователя
    public void deleteUser(String userId) {
        if (userRepository.getAllUsers().remove(userId) != null) {
            transactionRepository.getTransactions().remove(userId);
            System.out.println("Пользователь с ID " + userId + " удалён.");
        } else {
            System.out.println("Пользователь с таким ID не найден.");
        }
    }
}
