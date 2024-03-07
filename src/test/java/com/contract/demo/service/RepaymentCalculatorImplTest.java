package com.contract.demo.service;


import demo.contract.dto.RepaymentOffer;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.service.RepaymentCalculatorImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepaymentCalculatorImplTest {

    @Test
    void calculateRepayments() throws CustomExceptionHandler {
        RepaymentCalculatorImpl calculator = new RepaymentCalculatorImpl();

        // Mocking CustomExceptionHandler since we cannot throw real exceptions from the test
        CustomExceptionHandler exceptionHandler = mock(CustomExceptionHandler.class);
        when(exceptionHandler.getMessage()).thenReturn("Invalid input for repayment calculation");

        BigDecimal validAmount = BigDecimal.valueOf(1000);
        BigDecimal invalidAmount = BigDecimal.valueOf(-100);

        // Test with valid amount
        List<RepaymentOffer> validRepayments = calculator.calculateRepayments(validAmount);
        assertNotNull(validRepayments);
        assertEquals(3, validRepayments.size());

        RepaymentOffer offer12 = validRepayments.get(0);
        assertEquals(12, offer12.getRepaymentPeriod());
        // Add more assertions based on the expected values for offer12

        RepaymentOffer offer24 = validRepayments.get(1);
        assertEquals(24, offer24.getRepaymentPeriod());
        // Add more assertions based on the expected values for offer24

        RepaymentOffer offer36 = validRepayments.get(2);
        assertEquals(36, offer36.getRepaymentPeriod());
        // Add more assertions based on the expected values for offer36

        // Test with invalid amount
        try {
            calculator.calculateRepayments(invalidAmount);
            fail("Expected CustomExceptionHandler to be thrown");
        } catch (CustomExceptionHandler e) {
            assertEquals("Invalid input for repayment calculation", e.getMessage());
        }
    }

    @Test
    void calculateTotalRepayment() {
        RepaymentCalculatorImpl calculator = new RepaymentCalculatorImpl();

        BigDecimal deviceAmount = BigDecimal.valueOf(1000);
        int months = 12;

        double totalRepayment = calculator.calculateTotalRepayment(deviceAmount, months);

        assertTrue(totalRepayment > 0);
        assertEquals(1065.00, totalRepayment, 0.01);

    }
}

