package managers;
import java.time.*;
import java.util.*;
//import java.time.format.DateTimeFormatter;
import java.util.stream.*;

import database.*;
import models.*;
import services.AuthService;
import services.NotificationService;
import supports.TransactionType;

public class UserManager {
	private User user;
    private NotificationService notificationService;

    public UserManager(User user) {
        this.user = user;
        this.notificationService = new NotificationService(this);
    }

    public void setUser(User user) {
        if (this.user == null || !this.user.equals(user)) {
            this.user = user;
        }
    }

    public User getCurrentUser() {
        return user;
    }
//	public void registerUser(String name, String email, String password) {
//		try {
//			User user = new User(name, email, password);
//			UserRepository.getInstance().addUser(user);
//			System.out.println("User: " + user.getName() + " successfully registered!");
//		}catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//	}
	
	// Удаляем аккаунт пользователя
	
	public void deleteUser() {
		try {
			if(AuthService.getCurrentUser() != null) {
				AuthService.setCurrentUser(null);
			}
			UserRepository.getInstance().remove(user);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		 
	}
	
	// Редактируем профайл пользователя
	public void editUserProfile(String name, String email, String password) {
	    User currentUser = AuthService.getCurrentUser();
	    if (currentUser == null) {
	        System.out.println("Ошибка: Вы не вошли в систему!");
	        return;
	    }

	    // Если поля не переданы, оставляем старые значения
	    if (email != null && !email.isEmpty()) {
	        currentUser.setEmail(email);
	    }
	    if (password != null && !password.isEmpty()) {
	        currentUser.setPassword(password);
	    }
	    if (name != null && !name.isEmpty()) {
	        currentUser.setName(name);
	    }

	    System.out.println("Профиль обновлён!");
	}
	
	//Создаем новую транзакцию
	
	public void createTransaction(Double sum, String type, String category) {
	    User currentUser = getCurrentUser();  
	    if (currentUser == null) {
	        System.out.println("Ошибка: Пользователь не найден!");
	        return;
	    }

	    Transaction transaction;
	    if (type.equalsIgnoreCase("доход")) {
	        transaction = new Transaction(user.getId(), sum, category, TransactionType.INCOME);
	        user.setBalance(user.getBalance() + sum); 
	    } else {
	        if (user.getBalance() < sum) {
	            System.out.println("Не хватает средств для совершения транзакции!");
	            return;
	        }
	        transaction = new Transaction(user.getId(), sum, category, TransactionType.EXPENSE);
	        user.setBalance(user.getBalance() - sum);
	    }

	    // Добавляем транзакцию в репозиторий
	    TransactionRepository.getInstance().addTransaction(user.getId(), transaction);
	    System.out.println("Транзакция успешно создана!");

	    if (transaction.getType() == TransactionType.EXPENSE) {
	        checkBudget(); // Проверяем бюджет после добавления расхода
	    }
	}
	
	// Редактируем транзакцию и обновляем баланс при изменении суммы
	public void editTransaction(int transactionId, Double sum, String category) throws Exception {
	    List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(user.getId());

	    if (transactions == null || transactions.isEmpty()) {
	        throw new Exception("У вас нет текущих транзакций!");
	    }

	    Transaction transaction = transactions.stream()
	        .filter(t -> Integer.parseInt(t.getId()) == transactionId) // Исправленный поиск
	        .findFirst()
	        .orElse(null);

	    if (transaction == null) {
	        throw new Exception("Неправильный ID транзакции!");
	    }

	    double oldSum = transaction.getSum();
	    double newBalance = user.getBalance() + oldSum - sum;

	    if (newBalance < 0) {
	        System.out.println("Транзакцию невозможно изменить из-за нехватки средств!");
	        return;
	    }

	    user.setBalance(newBalance);
	    transaction.setSum(sum);
	    transaction.setCategory(category);
	    System.out.println("Транзакция успешно изменена!");
	}
	
	// Получаем все транзакции юзера
	
	public void getUserTransactions() {
	    List<Transaction> transactions = TransactionRepository.getInstance()
	        .getTransactions()
	        .getOrDefault(user.getId(), new ArrayList<>()); // Защита от null

	    if (transactions.isEmpty()) {
	        System.out.println("У вас нет текущих транзакций!");
	        return;
	    }

	    System.out.println("List of my transactions:");
	    transactions.forEach(t -> System.out.println("Transaction ID: " + t.getId() +
	        ", Sum: " + t.getSum() +
	        ", Transaction type: " + t.getType() +
	        ", Category: " + t.getCategory()));
	}
	
	// Удаляем транзакцию
	public void deleteTransaction(int transactionId) throws Exception {
		List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(user.getId());

		if(transactions.isEmpty()) {
			throw new Exception ("У вас нет текущих транзакций!");
			
		}
		boolean exists = transactions.stream().anyMatch(t -> t.getId().equals(String.valueOf(transactionId)));
		if (!exists) {
		    throw new Exception("Транзакция с таким ID не найдена!");
		}
		
		TransactionRepository.getInstance().removeTransaction(user.getId(), String.valueOf(transactionId));
		System.out.println("Транзакция успешна удалена!");
		
		
	}
	
    // Уведомляем пользователя о превышении бюджета при совершении транзакции с расходом
	public void checkBudget() {
		LocalDate now = LocalDate.now();
		List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(user.getId());
		
		double sum = transactions.stream()
				.filter(t -> t.getDate().getYear() == now.getYear() && t.getDate().getMonthValue() == now.getMonthValue())
				.map(Transaction::getSum)
				.reduce(0.0, Double::sum);
		
		if(sum > user.getMonthlyBudget()) {

			notificationService.sendBudgetLimitNotification(user.getEmail(), sum);
		}
		else if(sum == user.getMonthlyBudget()) System.out.println("Вы достигли свой месячный бюджет!");

	}
	
	// Дополнения для статистики и аналитики
		public double calculateBalance() {
			List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(user.getId());
		    double income = transactions.stream()
		            .filter(t -> t.getType() == TransactionType.INCOME)
		            .mapToDouble(Transaction::getSum)
		            .sum();
		    
		    double expense = transactions.stream()
		            .filter(t -> t.getType() == TransactionType.EXPENSE)
		            .mapToDouble(Transaction::getSum)
		            .sum();
		    
		    return income - expense;
		}
	
	// Устанавливаем новую цель (с приоритетом)
		
		public void createGoal(String name, String description, double targetAmount, int priority) {
		    if (getCurrentUser() == null) {
		        System.out.println("Ошибка: нет активного пользователя.");
		        return;
		    }

		    if (priority <= 0) {
		        System.out.println("Приоритет цели не может быть отрицательным числом или 0!");
		        return;
		    }

		    List<Goal> goals = GoalRepository.getInstance().getGoals()
		            .computeIfAbsent(user.getId(), k -> new ArrayList<>());

		    // Исправленный расчет `currentAmount`
		    double currentAmount = goals.stream()
		            .filter(g -> g.getPriority() <= priority) // фильтруем цели с приоритетом меньше или равным текущему
		            .map(Goal::getTargetAmount)
		            .reduce(user.getBalance(), (x, y) -> x - y);

		    if (currentAmount < 0.0) currentAmount = 0.0;

		    Goal goal = new Goal(user.getId(), name, description, targetAmount, currentAmount, priority);

		    GoalRepository.getInstance().addGoal(user.getId(), goal);

		    if (currentAmount >= targetAmount) {
		        notifyGoalReached(goal.getId(), name, currentAmount, targetAmount);
		    }

		    System.out.println("Новая цель успешно установлена!");
		}
//	public void createGoal(String name, String description, double targetAmount, int priority) {
//		if (getCurrentUser() == null) {
//		    System.out.println("Ошибка: нет активного пользователя.");
//		    return;
//		}
//		 List<Goal> goals = GoalRepository.getInstance().getGoals()
//			        .computeIfAbsent(user.getId(), k -> new ArrayList<>());
//		
//		double currentAmount = goals.stream().takeWhile(g -> g.getPriority() <= priority).map(Goal::getTargetAmount).reduce(user.getBalance(), (x, y)-> x - y);
//		if(currentAmount < 0.0) currentAmount = 0.0;
//		Goal goal = new Goal(user.getId(), name, description, targetAmount, currentAmount, priority);
//		if(currentAmount >= targetAmount) notifyGoalReached(goal.getId(), name, currentAmount, targetAmount);
//		
//		if(priority <= 0) {
//			System.out.println("Приоритет цели не может быть отрицательным числом или 0!");
//			return;
//		}
//		
//		GoalRepository.getInstance().addGoal(user.getId(), goal);
//		System.out.println("Новая цель успешно установлена!");
//		
//	}
		
		public void getUserGoals() {
		    if (getCurrentUser() == null) {
		        System.out.println("Ошибка: нет активного пользователя.");
		        return;
		    }

		    List<Goal> goals = GoalRepository.getInstance()
		        .getGoals()
		        .getOrDefault(getCurrentUser().getId(), new ArrayList<>());

		    if (goals.isEmpty()) {
		        System.out.println("У вас нет текущих целей!");
		        return;
		    }

		    System.out.println("Текущие цели:");
		    for (Goal goal : goals) {
		        System.out.println("ID: " + goal.getId() +
		                ", Название: " + goal.getName() +
		                ", Описание: " + goal.getDescription() +
		                ", Целевая сумма: " + goal.getTargetAmount() +
		                ", Текущий баланс: " + goal.getCurrentAmount() +
		                ", Приоритет: " + goal.getPriority());
		    }
		}
	
	

	// Уведомляем пользователя если цель достигнута так же спрашиваем вложить в цель или нет
	public void notifyGoalReached(String id, String name, double currentAmount, double targetAmount) {
		if(currentAmount >= targetAmount) {
			currentAmount = targetAmount;
			notificationService.sendGoalAchievedNotification(user.getEmail(), GoalRepository.getInstance().getGoals().get(user.getId()).stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null));
			System.out.println("У вас цель: " + "{"+ name +"} достигнута! \n Хотите вложить в свою цель? (Да/Нет)"); 
			Scanner in = new Scanner(System.in);
			String reply = in.nextLine();
			while(reply.toLowerCase().equals("нет")) {
				if(reply.toLowerCase().equals("да")) {
					contributeToGoal(id, name, currentAmount);
					return;
				}
				System.out.println("Пожалуйста ответьте Да или Нет!");
				reply = in.nextLine();
			}
			
		}
		
	}
	
	// Создаем новую транзакцию чтобы завершить цель
	public void contributeToGoal(String goalId, String name, double amount) {
		if(amount <= user.getBalance()) {
			this.createTransaction(amount, "расход", "Вклад в цель");
			GoalRepository.getInstance().removeGoal(user.getId(), goalId);
			System.out.println("Поздравляю! Вы вложили в цель!");
		}
		
		
	}
	// Обновляем прогресс Целей при совершении транзакции
	public void updateGoalProgress() {
		if (user == null) {
	        System.out.println("Ошибка: пользователь не найден.");
	        return;
	    }

	    List<Goal> goals = GoalRepository.getInstance().getGoals().get(user.getId());

	    if (goals == null || goals.isEmpty()) {
	        System.out.println("У вас пока нет целей.");
	        return;
	    }

	    double sum = user.getBalance();

	    for (Goal g : goals) {
	        if (sum >= g.getTargetAmount()) {
	            if (g.getCurrentAmount() != g.getTargetAmount()) {
	                g.setCurrentAmount(g.getTargetAmount());
	                notifyGoalReached(g.getId(), g.getName(), g.getCurrentAmount(), g.getTargetAmount());
	            }
	            sum -= g.getTargetAmount();
	        } else {
	            g.setCurrentAmount(sum);
	            sum = 0;
	            break;
	        }
	    }

	}
	
	public void printFinancialReport() {
		System.out.println("Финансовый отчет: " + createFinancialReport());
		
	}
	
	public FinancialReport createFinancialReport() {
	    Scanner in = new Scanner(System.in);
	    System.out.println("Для создания Финансового Отчета, пожалуйста, введите период, за который вы совершали транзакции!");

	    LocalDate startDate, endDate;

	    // Ввод даты начала
	    while (true) {
	        try {
	            System.out.print("Введите день начала: ");
	            int startDay = in.nextInt();

	            System.out.print("Введите месяц начала: ");
	            int startMonth = in.nextInt();

	            System.out.print("Введите год начала: ");
	            int startYear = in.nextInt();

	            startDate = LocalDate.of(startYear, startMonth, startDay);

	            if (startDate.isAfter(LocalDate.now())) {
	                System.out.println("Введена будущая дата! Попробуйте еще раз.");
	                continue;
	            }
	            break;
	        } catch (Exception e) {
	            System.out.println("Ошибка ввода! Введите корректные числа.");
	            in.nextLine(); 
	        }
	    }

	    // Ввод даты конца
	    while (true) {
	        try {
	            System.out.print("Введите день конца: ");
	            int endDay = in.nextInt();

	            System.out.print("Введите месяц конца: ");
	            int endMonth = in.nextInt();

	            System.out.print("Введите год конца: ");
	            int endYear = in.nextInt();

	            endDate = LocalDate.of(endYear, endMonth, endDay);

	            if (endDate.isAfter(LocalDate.now()) || endDate.isBefore(startDate)) {
	                System.out.println("Некорректный диапазон дат! Попробуйте еще раз.");
	                continue;
	            }
	            break;
	        } catch (Exception e) {
	            System.out.println("Ошибка ввода! Введите корректные числа.");
	            in.nextLine(); 
	        }
	    }

	    // Создание отчета
	    FinancialReport report = new FinancialReport(user.getId(), startDate, endDate);
	    System.out.println("Вы успешно создали Финансовый Отчет!");
	    return report;
	}
	
	//Анализ расхода по категориям
	
	public Map<String, Double> analyzeExpensesByCategory(String userId) {
	    List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(userId);

	    if (transactions == null || transactions.isEmpty()) {
	        return Collections.emptyMap(); // Если транзакций нет, возвращаем пустую карту
	    }

	    return transactions.stream()
	        .filter(t -> t.getType() == TransactionType.EXPENSE) // Фильтруем только расходы
	        .collect(Collectors.groupingBy(
	            Transaction::getCategory,   // Группируем по категории
	            Collectors.summingDouble(Transaction::getSum) // Суммируем расходы по каждой категории
	        ));
	}
	
	// Формирование отчёта для пользователя по финансовому состоянию
	public void calculateNetWorth() {
		// Создаем финансовый отчет для вычисления финансового состояния пользователя
		FinancialReport report = new FinancialReport(user.getId());
		System.out.println("Ваше текущее финансовое состояние: " + report.getNetWorth());
	}
	
	

//	public User getUser() {
//		return user;
//	}

	
	
	

}
