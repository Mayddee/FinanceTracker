package models;

import java.util.*;

import supports.TransactionType;

import java.time.*;

public class Transaction {
	private static int index = 0;
	private String id;
	private String userID;
	private double sum;
	private String category;
	private TransactionType type;
	private LocalDateTime date;

	{
		++index;
	}
	public Transaction() {
		
	}

	
	public Transaction(String userID) {
		this.id = String.valueOf(index);
		this.userID = userID;
	}
	
	public Transaction(String userID, double sum, String category, TransactionType type) {
		this(userID);
		this.sum = sum;
		this.category = category;
		this.type = type;
		this.date = LocalDateTime.now();
		
	}
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(o.getClass() != this.getClass()) return false;
		Transaction other = (Transaction) o;
		return this.id.equals(other.id) && this.userID.equals(other.userID) && this.sum == other.sum && this.category.equals(other.category) && this.type.equals(other.type);
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


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
	
    @Override
    public String toString() {
        return "Transaction {" +
               "id='" + id + '\'' +
               ", userID='" + userID + '\'' +
               ", sum=" + sum +
               ", category='" + category + '\'' +
               ", type=" + type +
               ", date=" + date +
               '}';
    }
	

}
