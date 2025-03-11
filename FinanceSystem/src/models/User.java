package models;
import java.util.*;

import database.TransactionRepository;
import managers.UserManager;
import supports.*;
import supports.Observer;



import java.time.*;

public class User implements Observer {
	private String id;
	private String name;
	private String email;
	private String password;
	private double balance;
	private LocalDate registerDate;
	private double monthlyBudget;
	private UserManager manager;
	private boolean isBlocked; 
	
	public User() {
		
		this.isBlocked = false;
		this.manager = new UserManager(this);
	}
	
	public User(String name, String email, String password) throws Exception{
		this();
		this.setId(UUID.randomUUID().toString());
		this.setName(name);
		if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			
			throw new Exception("Invalid Email format!");
		}
		this.setEmail(email);
		this.setPassword(password);
		this.setBalance(0.0);
		setRegisterDate(LocalDate.now());
		
	}
	
	public User(String name, String email, String password, double monthlyBudget) throws Exception{
		this(name, email, password);
		this.setMonthlyBudget(monthlyBudget);
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}
	
	// Обновляем цели пользователя для проверки прогресса при изменении баланса 
	public void setBalance(double balance) {
		this.balance = balance;
		manager.updateGoalProgress();
	}

	public LocalDate getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public double getMonthlyBudget() {
		return monthlyBudget;
	}

	public void setMonthlyBudget(double monthlyBudget) {
		this.monthlyBudget = monthlyBudget;
	}
	
	public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
    
    public UserManager getUserManager() {
        return manager;
    }

	@Override
	public void update(String message) {
        System.out.println("Уведомление для " + email + ": " + message);
		
	}

	

	

}
