package demo.contract.service;

import demo.api.User.V1.UserRegistrationRequest;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserRegistrationRequest registerUser(UserRegistrationRequest registrationRequest) throws CustomExceptionHandler;
    User searchUser(String username) throws CustomExceptionHandler;
    boolean authenticateUser(String username, String password) throws CustomExceptionHandler;
}
