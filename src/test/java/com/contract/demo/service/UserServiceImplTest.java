package com.contract.demo.service;

import demo.api.User.V1.UserRegistrationRequest;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.model.User;
import demo.contract.repository.UserRepository;
import demo.contract.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_SuccessfulRegistration() throws CustomExceptionHandler {
        // Mock data
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setPassword("password1234");
        registrationRequest.setUsername("testUser");
        registrationRequest.setEmail("john@example.com");
        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(null);
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");

        // Perform registration
        UserRegistrationRequest result = userService.registerUser(registrationRequest);

        // Assertions
        assertNotNull(result);
        assertEquals(registrationRequest.getUsername(), result.getUsername());
        assertEquals(registrationRequest.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyTaken() {
        // Mock data
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setPassword("password1234");
        registrationRequest.setUsername("testUser");
        registrationRequest.setEmail("john@example.com");
        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(new User());

        // Perform registration and expect an exception
        assertThrows(CustomExceptionHandler.class, () -> userService.registerUser(registrationRequest));

        // Verify interactions
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyRegistered() {
        // Mock data
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setPassword("password1234");
        registrationRequest.setUsername("testUser");
        registrationRequest.setEmail("john@example.com");
        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(null);
        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        // Perform registration and expect an exception
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registrationRequest));

        // Verify interactions
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_ValidCredentials() {
        // Mock data
        User user = new User();
        user.setPassword("password1234");
        user.setUsername("testUser");
        user.setEmail("john@example.com");
        when(userRepository.findByUsername("john_doe")).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        // Perform authentication
        boolean result = userService.authenticateUser("john_doe", "password");

        // Assertion
        assertTrue(result);
    }

    @Test
    void authenticateUser_InvalidUsername() {
        // Mock data
        when(userRepository.findByUsername("non_existing_user")).thenReturn(null);

        // Perform authentication and expect false result
        boolean result = userService.authenticateUser("non_existing_user", "password");

        // Assertion
        assertFalse(result);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticateUser_InvalidPassword() {
        // Mock data
        User user = new User();
        user.setPassword("encodedPassword");
        user.setUsername("testUser");
        user.setEmail("john@example.com");
        // Corrected stubbing for findByUsername
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        // Perform authentication and expect false result
        boolean result = userService.authenticateUser("john_doe", "wrong_password");

        // Assertion
        assertFalse(result);
    }


    @Test
    void searchUser_UserFound() throws CustomExceptionHandler {
        // Mock data
        User user = new User();
        user.setPassword("password1234");
        user.setUsername("testUser");
        user.setEmail("john@example.com");
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        // Perform user search
        User result = userService.searchUser("john_doe");

        // Assertion
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void searchUser_UserNotFound() {
        // Mock data
        when(userRepository.findByUsername("non_existing_user")).thenReturn(null);

        // Perform user search and expect an exception
        assertThrows(CustomExceptionHandler.class, () -> userService.searchUser("non_existing_user"));
    }
}

