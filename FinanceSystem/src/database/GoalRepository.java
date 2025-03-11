package database;

import java.util.*;

import models.Goal;
import models.Transaction;
import supports.GoalPriorityComparator;

public class GoalRepository {
	private static final GoalRepository INSTANCE = new GoalRepository();
	// Храним список транзакций по userID
    private Map<String, List<Goal>> goals = new HashMap<>();

    private GoalRepository() {}

	


    public static GoalRepository getInstance() {
        return INSTANCE;
    }

    public void addGoal(String userId, Goal goal) {
        goals.computeIfAbsent(userId, k -> new ArrayList<>()).add(goal);
        List<Goal> userGoals = goals.get(userId);
        
        if (userGoals != null && userGoals.size() > 1) {
            userGoals.sort(new GoalPriorityComparator()); // Сортируем цели по приоритету
        }
    }

    public void removeGoal(String userId, String goalId) {
        goals.getOrDefault(userId, new ArrayList<>())
            .removeIf(t -> t.getId().equals(goalId));
    }
    
    public Map<String, List<Goal> > getGoals() {
		return goals;
	}

	public void setGoals(Map<String, List<Goal>> goals) {
		this.goals = goals;
	}
}
