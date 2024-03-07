package demo.contract.controller;

import demo.api.User.V1.UserLoginRequest;
import demo.api.User.V1.UserRegistrationRequest;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.model.User;
import demo.contract.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/v1")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationRequest registrationRequest) throws CustomExceptionHandler {
        log.info("register api invoked...");
        UserRegistrationRequest registeredUser = userService.registerUser(registrationRequest);
        log.info("User successfully registered.");
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login/v1")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest loginRequest) {
        log.info("Performing login...");
        try {
            // Check if the user exists and password matches
            boolean authenticated = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

            if (authenticated) {
                log.info("Login successful!");
                return new ResponseEntity<>("Login successful!", HttpStatus.OK);
            } else {
                log.info("Login unsuccessful!");
                return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
            }
        } catch (CustomExceptionHandler e) {
            log.error("Exception during login: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{username}/v1")
    public ResponseEntity<Object> searchUser(@PathVariable String username) throws CustomExceptionHandler {
        User optionalUser = userService.searchUser(username);
        log.info("Searching for user...");
        if (optionalUser != null) {
            User foundUser = optionalUser;
            log.info("User found...");
            return ResponseEntity.ok(foundUser);
        } else {
            log.info("User not found...");
             return ResponseEntity.notFound().build();
        }
    }
}

