package database;
import java.util.*;
import models.*;

public class TransactionRepository {
	private static final TransactionRepository INSTANCE = new TransactionRepository();
	// Храним список транзакций по userID
    private Map<String, List<Transaction>> transactions = new HashMap<>();

    private TransactionRepository() {}


    public static TransactionRepository getInstance() {
        return INSTANCE;
    }

    public void addTransaction(String userId, Transaction transaction) {
        transactions.computeIfAbsent(userId, k -> new ArrayList<>()).add(transaction);
        System.out.println("Добавлена новая транзакция: " + transaction.getId() + " для пользователя " + userId);
    }

    public void removeTransaction(String userId, String transactionId) {
        transactions.getOrDefault(userId, new ArrayList<>())
            .removeIf(t -> t.getId().equals(transactionId));
    }
    
   
    
    public Map<String, List<Transaction> > getTransactions() {
		return transactions;
	}

	public void setTransactions(Map<String, List<Transaction>> transactions) {
		this.transactions = transactions;
	}
}
