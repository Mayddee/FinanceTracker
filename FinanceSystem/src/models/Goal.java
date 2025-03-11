package models;

import java.util.*;

public class Goal {
	private static int index = 0;
	private String id;
	private String userID;
	private String name;
	private String description;
	private double targetAmount;
	private double currentAmount;
	private int priority;
	
	
	public Goal() {
		index++;
	}
	
	public Goal(String userID) {
		this();
		this.id = String.valueOf(index);
		this.userID = userID;
	}
	
	public Goal(String userID, String name, String description, double targetAmount, double currentAmount) {
		this(userID);
		this.name = name;
		this.setDescription(description);
		this.setTargetAmount(targetAmount);
		this.setCurrentAmount(currentAmount);
		
	}
	public Goal(String userID, String name, String description, double targetAmount, double currentAmount, int priority) {
		this(userID, name, description, targetAmount, currentAmount);
		this.setPriority(priority);
		
	}
	
	
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(o.getClass() != this.getClass()) return false;
		Goal other = (Goal) o;
		return this.id.equals(other.id) && this.userID.equals(other.userID) && this.name.equals(other.name);
	}

	public double getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(double targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	



}
