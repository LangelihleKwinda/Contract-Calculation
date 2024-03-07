package demo.contract.controller;

import demo.contract.dto.RepaymentOffer;
import demo.contract.exception.CustomExceptionHandler;
import demo.contract.service.RepaymentCalculatorImpl;
import demo.contract.service.RepaymentCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/devices")
@Slf4j
public class DeviceController {

    private final RepaymentCalculatorService repaymentCalculatorService;

    @Autowired
    public DeviceController(RepaymentCalculatorImpl deviceService) {
        this.repaymentCalculatorService = deviceService;
    }

    @PostMapping("/calculateRepayments/{deviceAmount}/v1")
    public ResponseEntity<Object> calculateRepayments(@PathVariable BigDecimal deviceAmount) {
        log.info("Calculate Repayments api invoked...");
        try {
            // Validate input and perform calculations
            if (deviceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.info("Device amount must be greater than 0");
                throw new CustomExceptionHandler("Device amount must be greater than 0");
            }

            List<RepaymentOffer> repaymentResponse = repaymentCalculatorService.calculateRepayments(deviceAmount);
            return new ResponseEntity<>(repaymentResponse, HttpStatus.OK);

        } catch (CustomExceptionHandler ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


