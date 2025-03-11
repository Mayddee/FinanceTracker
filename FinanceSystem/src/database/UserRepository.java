package database;

import java.security.KeyPair;
import java.util.*;

import models.*;

public class UserRepository {
	private static final UserRepository INSTANCE = new UserRepository();
	private Map<String,User> users = new HashMap<String,User>();
	
	
	private UserRepository() {}

    public static UserRepository getInstance() {
        return INSTANCE;
    }

	
	public void addUser(User user) throws Exception {
		if(userExists(user.getEmail())) throw new Exception("User with the email " + user.getEmail() + " already exists!");
		users.put(user.getId(), user);
	}
	
	public void remove(User user) throws Exception {
		if(!users.containsKey(user.getId())) throw new Exception("Such user does not exist!");
		
		users.remove(user.getId());
	}
	
	//Получаем юзера через email
	
	public User getUserByEmail(String email) {
	    return users.values().stream()
	            .filter(user -> user.getEmail().equals(email))
	            .findFirst()
	            .orElse(null);
	}
	
	
	
	public User getUserByID(String id) {
		return users.get(id);
	}
	
	public Map<String, User> getAllUsers() {
        return users;
    }
	
	public boolean userExists(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));

	}

}
