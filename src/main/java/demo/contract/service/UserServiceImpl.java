package demo.contract.service;

import demo.api.User.V1.UserRegistrationRequest;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.model.User;
import demo.contract.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegistrationRequest registerUser(UserRegistrationRequest registrationRequest) throws CustomExceptionHandler {
        // Check if the username is already taken
        log.info("Registering user...");
        if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            log.info("Username is already taken!!!");
            throw new CustomExceptionHandler("Username is already taken");
        }

        // Check if the email is already registered
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            log.info("Email already registered!!!");
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash the user's password before saving it to the database
        log.info("Encoding password...");
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        // Create a new User entity with the provided details
        User userEntity = new User();
        userEntity.setUsername(registrationRequest.getUsername());
        userEntity.setEmail(registrationRequest.getEmail());
        userEntity.setPassword(encodedPassword);

        // Save the user in the database
        userRepository.save(userEntity);
        log.info("New user successfully saved.");

        // Return the registration request (you might consider returning a User object instead)
        return registrationRequest;
    }

    public boolean authenticateUser(String username, String password) {
        log.info("Verifying the user exists...");
        User user = userRepository.findByUsername(username);

        // Check if the user exists
        if (user == null) {
            log.info("User does not exist.");
            return false; // User not found, return false
        }

        // Check if the entered password matches the stored password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("Password and email do not match");
            return false; // Invalid password, return false
        }

        return true;
    }



    @Override
    public User searchUser(String username) throws CustomExceptionHandler {
        // Retrieve the user by username
        log.info("Retrieving user by username....");
        User user = userRepository.findByUsername(username);

        // Check if the user exists
        if (user == null) {
            log.info("User does not exists");
            throw new CustomExceptionHandler("User not found");
        }

        return user;
    }
}
