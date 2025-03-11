package models;
import java.util.*;
import java.time.*;

import database.TransactionRepository;
import database.UserRepository;
import supports.TransactionType;


public class FinancialReport {
    private String id;
    private String userID;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalIncome;
    private double totalOutcome;
    private double netWorth;


    public FinancialReport() {
        this.id = UUID.randomUUID().toString();
    }

    public FinancialReport(String userID) {
        this();
        this.userID = userID;
        this.startDate = UserRepository.getInstance().getAllUsers().get(userID).getRegisterDate();
        this.endDate = LocalDate.now();
        calculateNetWorth();
    }

    public FinancialReport(String userID, LocalDate startDate, LocalDate endDate) {
        this();
        this.userID = userID;
        this.startDate = startDate;
        this.endDate = endDate;
        calculateNetWorth();
    }

    private void calculateNetWorth() {
        List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(userID);
        
        if (transactions == null) { // Если транзакций нет
            this.totalIncome = 0.0;
            this.totalOutcome = 0.0;
            this.netWorth = 0.0;
            return;
        }

        // Фильтрация по диапазону дат
        this.totalIncome = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME && isDateInRange(t.getDate().toLocalDate()))
            .mapToDouble(Transaction::getSum)
            .sum();

        this.totalOutcome = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE && isDateInRange(t.getDate().toLocalDate()))
            .mapToDouble(Transaction::getSum)
            .sum();

        this.netWorth = this.totalIncome - this.totalOutcome;
    }

    // Метод для удобного сравнения дат
    private boolean isDateInRange(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) && 
               (date.isEqual(endDate) || date.isBefore(endDate));
    }
    
	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public double getTotalIncome() {
		return totalIncome;
	}

	public double getTotalOutcome() {
		return totalOutcome;
	}

	public double getNetWorth() {
		return netWorth;
	}

}