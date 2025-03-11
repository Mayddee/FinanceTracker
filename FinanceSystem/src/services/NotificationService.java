package services;

import java.util.ArrayList;
import java.util.List;

import database.UserRepository;
import managers.UserManager;
import models.Goal;
import models.User;
import supports.Observer;

public class NotificationService {
    private List<Observer> observers = new ArrayList<>();
    private UserManager userManager;

    public NotificationService(UserManager userManager) {
        this.userManager = userManager;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void sendBudgetLimitNotification(String userEmail, double overBudgetAmount) {
        User user = UserRepository.getInstance().getUserByEmail(userEmail);
        if (user != null) {
            String message = "⚠ ВНИМАНИЕ! Вы превысили месячный бюджет на " + overBudgetAmount + "!";
            System.out.println("УВЕДОМЛЕНИЕ: " + message);
            notifyObservers(message);
            sendEmailNotification(user.getEmail(), "Превышение бюджета", message);
        }
    }

    public void sendGoalAchievedNotification(String userEmail, Goal goal) {
        User user = UserRepository.getInstance().getUserByEmail(userEmail);
        if (user != null) {
            String message = "Поздравляем! Вы достигли цели: " + goal.getName();
            System.out.println("УВЕДОМЛЕНИЕ: " + message);
            notifyObservers(message);
            sendEmailNotification(user.getEmail(), "Цель достигнута!", message);
        }
    }

    private void sendEmailNotification(String email, String subject, String message) {
        System.out.println("Отправка email на " + email + ": " + subject);
    }
}