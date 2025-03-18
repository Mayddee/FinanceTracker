package managers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import database.TransactionRepository;
import junit.framework.Assert;
import models.Transaction;
import models.User;
import services.AuthService;

class UserManagerTest {
	
	private User user;
	
	@BeforeEach
	void setUserManager() throws Exception {
		user = AuthService.registerUser("Madina", "a.madikosh2004@gmail.com", "password1");
		AuthService.login("a.madikosh2004@gmail.com", "password");
		
		
	}

	@Test
	public void deleteUser() {
		user.getUserManager().deleteUser();
		if(user.equals(null)) {
			fail("User is not deleted!");
		}
	}
	
	@Test void editUserProfile() {
		user.getUserManager().editUserProfile("Madosh", "", "pass");
		User actual = user;
		try {
			User expected = new User("Madosh", "", "pass");
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Actual name:" + actual.getName());
		if(!actual.getName().equals("Madosh")) {
			fail("Username is not changed!");
		}
		if(!actual.getEmail().equals("a.madikosh2004@gmail.com")) {
			fail("Email is not correct!");
		}
		if(!actual.getPassword().equals("pass")) {
			fail("Password is not changed!");
		}
		
	}
	
	@Test 
	public void createTransaction() {
		Transaction transaction = user.getUserManager().createTransaction(1200000.0, "доход", "Зарплата");
		List<Transaction> transactions = TransactionRepository.getInstance().getTransactions().get(user.getId());
		if(transactions == null) {
			
			fail("Transaction list is null!");
		}
		if(transaction == null) {
			fail("Transaction is not created even!");
		}
		if(transactions.contains(transaction)) {
			fail("Transaction could not be added to transaction list!");
		}
	}
	
	

}
