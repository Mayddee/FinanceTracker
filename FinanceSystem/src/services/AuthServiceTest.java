package services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import database.UserRepository;
import models.User;

class AuthServiceTest {
	UserRepository userRepository;
	User user1;
	User user2;
	
	@BeforeEach
	void register() {
		user1 = AuthService.registerUser("Madosh", "mad2@kbtu.kz", "password2");
//		user1.setId("123");
		userRepository = Mockito.mock(UserRepository.class);
		user2 = AuthService.registerUser("Madina", "mad@kbtu.kz", "password1");
//		user2.setId("234");
//		System.out.println("Email: " + user2.getEmail());
		

        Mockito.when(userRepository.getUserByEmail("mad@kbtu.kz")).thenReturn(user2);
        Mockito.when(userRepository.userExists("mad@kbtu.kz")).thenReturn(true);
	}

	@Test
	void registerUser() {
		

		Map<String, User> users = new HashMap<String, User>();
		users.put(user2.getId(), user2);
		Mockito.when(userRepository.getAllUsers()).thenReturn(users);
		Map<String, User> allUsers = userRepository.getAllUsers();
		if(!allUsers.containsKey(user2.getId())) {
			fail("User is not registered!");
		}
	}
	
	@Test
	void login() {
//		
//		Mockito.when(userRepository.getUserByEmail("mad@kbtu.kz")).thenReturn(user2);
//		User expected = userRepository.getUserByEmail("mad@kbtu.kz");
//		AuthService.login("mad@kbtu.kz", "password1");
//		User actual = AuthService.getCurrentUser();
////		Mockito.verify(userRepository, times(1)).getUserByEmail("mad@kbtu.kz");
//		
////		User expected = UserRepository.getInstance().getUserByEmail("mad@kbtu.kz");
//		Assert.assertEquals(expected, actual);
		
		try (MockedStatic<UserRepository> mockedStatic = Mockito.mockStatic(UserRepository.class)) {
            mockedStatic.when(UserRepository::getInstance).thenReturn(userRepository);

            AuthService.login("mad@kbtu.kz", "password1");

            User actual = AuthService.getCurrentUser();
            Mockito.verify(userRepository, times(1)).getUserByEmail("mad@kbtu.kz");

            assertEquals(user2, actual);
        }
	}
	
	@Test 
	void logout (){
        AuthService.logout();

		Assert.assertNull("User is not logged out!", AuthService.getCurrentUser());
	}

}
