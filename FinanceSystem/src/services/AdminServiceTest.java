package services;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import database.TransactionRepository;
import database.UserRepository;
import models.Transaction;
import models.User;
import supports.TransactionType;

class AdminServiceTest {
    
    private UserRepository mockUserRepository;
    private TransactionRepository mockTransactionRepository;
    private AdminService adminService;
    private User user;
    
    @BeforeEach
    void setUp() throws Exception {
        mockUserRepository = Mockito.mock(UserRepository.class);
        mockTransactionRepository = Mockito.mock(TransactionRepository.class);
        adminService = new AdminService(mockUserRepository, mockTransactionRepository);

        user = new User("Madina", "mad@kbtu.kz", "pass1");
        user.setId("user123"); 
    }

    @Test
    void BlockUser() {
        // Mock the user repository to return our test user
        Map<String, User> users = new HashMap<>();
        users.put("user123", user);
        when(mockUserRepository.getAllUsers()).thenReturn(users);

        adminService.blockUser("user123");
        assertTrue(user.isBlocked(), "User should be blocked.");
    }

    @Test
    void DeleteUser() {
        Map<String, User> users = new HashMap<>();
        users.put("user123", user);
        Map<String, List<Transaction>> transactions = new HashMap<>();
        transactions.put("user123", new ArrayList<>());

        when(mockUserRepository.getAllUsers()).thenReturn(users);
        when(mockTransactionRepository.getTransactions()).thenReturn(transactions);

        adminService.deleteUser("user123");

        assertNull(users.get("user123"), "User should be deleted.");
        assertNull(transactions.get("user123"), "Transactions should be removed.");
    }
}