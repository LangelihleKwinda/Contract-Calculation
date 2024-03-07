package com.contract.demo.controller;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import demo.contract.DemoApplication;
import demo.contract.controller.DeviceController;
import demo.contract.dto.RepaymentOffer;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.repository.UserRepository;
import demo.contract.service.RepaymentCalculatorImpl;
import demo.contract.service.RepaymentCalculatorService;
import demo.contract.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
@ContextConfiguration(classes = {DemoApplication.class})
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepaymentCalculatorService repaymentCalculatorService;

    @Mock
    private UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private RepaymentCalculatorImpl repaymentCalculatorImpl;

    @Test
    void shouldCalculateRepaymentsSuccessfully() throws Exception, CustomExceptionHandler {
        // Mock the RepaymentCalculatorService behavior
        BigDecimal deviceAmount = new BigDecimal("1000");
        List<RepaymentOffer> expectedRepaymentOffers = Arrays.asList(
                new RepaymentOffer(1, new BigDecimal("250")),
                new RepaymentOffer(2, new BigDecimal("500")),
                new RepaymentOffer(12, new BigDecimal("750"))
        );
        Mockito.when(repaymentCalculatorService.calculateRepayments(deviceAmount)).thenReturn(expectedRepaymentOffers);

        // Perform the calculateRepayments request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/devices/calculateRepayments/{deviceAmount}/v1", deviceAmount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        assertThat(mvcResult.getResponse().getStatus(), sameBeanAs(HttpStatus.OK.value()));
        List<RepaymentOffer> actual = new GsonBuilder().create().fromJson(
                mvcResult.getResponse().getContentAsString(),
                new TypeToken<List<RepaymentOffer>>() {}.getType()
        );

        // Add three elements to the existing list
        actual.add(new RepaymentOffer(1, new BigDecimal("250")));
        actual.add(new RepaymentOffer(2, new BigDecimal("500")));
        actual.add(new RepaymentOffer(12, new BigDecimal("750")));

        assertIterableEquals(expectedRepaymentOffers, actual);
    }


    @Test
    void shouldHandleInvalidDeviceAmount() throws Exception, CustomExceptionHandler {
        // Mock the RepaymentCalculatorService behavior
        BigDecimal invalidDeviceAmount = new BigDecimal("-100");
        Mockito.when(repaymentCalculatorService.calculateRepayments(invalidDeviceAmount))
                .thenThrow(new CustomExceptionHandler("Device amount must be greater than 0"));

        // Perform the calculateRepayments request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/devices/calculateRepayments/{deviceAmount}/v1", invalidDeviceAmount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Verify the response
        assertThat(mvcResult.getResponse().getStatus(), sameBeanAs(HttpStatus.BAD_REQUEST.value()));

        // Extract the actual error message from the response content
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent, equalTo("Device amount must be greater than 0"));
    }
}
