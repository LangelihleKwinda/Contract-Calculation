package com.contract.demo.controller;

import com.google.gson.GsonBuilder;
import demo.api.User.V1.UserLoginRequest;
import demo.api.User.V1.UserRegistrationRequest;
import demo.contract.DemoApplication;
import demo.contract.controller.UserController;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.model.User;
import demo.contract.repository.UserRepository;
import demo.contract.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@ContextConfiguration(classes = {DemoApplication.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @MockBean
    UserRepository userRepository;
    User domainUser;
    UserLoginRequest userLoginRequest;
    UserRegistrationRequest userRegistrationRequest;
    UserRegistrationRequest registeredUser;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        domainUser = User.builder().id(1L).username("testUser").password("password123").email("test@example.com").build();

        userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("testUser");
        userLoginRequest.setPassword("password123");

        userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUsername("testUser");
        userRegistrationRequest.setPassword("test@example.com");
        userRegistrationRequest.setPassword("password123");

        registeredUser = new UserRegistrationRequest();
        registeredUser.setUsername("testUser");
        registeredUser.setPassword("test@example.com");
        registeredUser.setPassword("password123");

        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
    }

    @Test
    void shouldSuccessfullyRegisterUser() throws Exception, CustomExceptionHandler {
        // Mock the UserService behavior
        Mockito.when(userService.registerUser(Mockito.any(UserRegistrationRequest.class))).thenReturn(registeredUser);

        // Perform the registration request
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/register/v1")
                        .content(new GsonBuilder().create().toJson(userRegistrationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // Verify the response
        assertThat(mvcResult.getResponse().getStatus(), sameBeanAs(HttpStatus.CREATED.value()));
        UserRegistrationRequest actual = new GsonBuilder().create().fromJson(mvcResult.getResponse().getContentAsString(), UserRegistrationRequest.class);
        assertThat(actual, sameBeanAs(registeredUser));
    }

    @Test
    void shouldHandleInvalidPasswordDuringLogin() throws Exception, CustomExceptionHandler {
        // Mock the UserService behavior for successful login without exceptions
        Mockito.when(userService.authenticateUser(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        // Mock the UserRepository to return a user when findByUsername is called
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(domainUser);

        // Mock the passwordEncoder.matches to return false, indicating an invalid password
        Mockito.when(passwordEncoder.matches(registeredUser.getUsername(), registeredUser.getPassword())).thenReturn(true);

        // Perform the login request
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login/v1")
                        .content(new GsonBuilder().create().toJson(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // Expecting Unauthorized status
                .andReturn();

        // Print the response content for debugging
        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        // Verify the response
        assertThat(mvcResult.getResponse().getStatus(), sameBeanAs(HttpStatus.UNAUTHORIZED.value()));
        assertThat(responseContent, equalTo("Invalid username or password."));
    }



    @Test
    void shouldReturnUnauthorizedForInvalidLogin() throws Exception, CustomExceptionHandler {
        // Mock the UserService behavior for unsuccessful login
        Mockito.when(userService.authenticateUser(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        // Perform the login request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login/v1")
                        .content(new GsonBuilder().create().toJson(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        // Verify the response
        assertThat(mvcResult.getResponse().getStatus(), sameBeanAs(HttpStatus.UNAUTHORIZED.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), equalTo("Invalid username or password."));
    }
}
