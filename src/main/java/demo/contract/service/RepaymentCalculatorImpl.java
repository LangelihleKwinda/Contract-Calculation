package demo.contract.service;

import demo.contract.dto.RepaymentOffer;
import demo.contract.exception.CustomExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RepaymentCalculatorImpl implements RepaymentCalculatorService {

    private static final double INTEREST_RATE = 0.065; // 6.5%

    @Override
    public List<RepaymentOffer> calculateRepayments(BigDecimal deviceAmount) throws CustomExceptionHandler {
        // Validate input
        if (deviceAmount == null || deviceAmount == null || deviceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("Invalid input for repayment calculation");
            throw new CustomExceptionHandler("Invalid input for repayment calculation");
        }

        BigDecimal amount = deviceAmount;
        List<RepaymentOffer> repaymentOffers = new ArrayList<>();

        // Calculate repayment for 12 months
        RepaymentOffer offer12 = new RepaymentOffer();
        offer12.setRepaymentPeriod(12);
        offer12.setRepaymentAmount(BigDecimal.valueOf(calculateTotalRepayment(amount, 12)));
        repaymentOffers.add(offer12);

        // Calculate repayment for 24 months
        RepaymentOffer offer24 = new RepaymentOffer();
        offer24.setRepaymentPeriod(24);
        offer24.setRepaymentAmount(BigDecimal.valueOf(calculateTotalRepayment(amount, 24)));
        repaymentOffers.add(offer24);

        // Calculate repayment for 36 months
        RepaymentOffer offer36 = new RepaymentOffer();
        offer36.setRepaymentPeriod(36);
        offer36.setRepaymentAmount(BigDecimal.valueOf(calculateTotalRepayment(amount, 36)));
        repaymentOffers.add(offer36);

        return repaymentOffers;
    }

    public double calculateTotalRepayment(BigDecimal deviceAmount, int months) {
        // Calculate interest
        log.info("Calculating " + months + " interest.");
        BigDecimal interest = deviceAmount.multiply(BigDecimal.valueOf(INTEREST_RATE)).multiply(BigDecimal.valueOf(months / 12.0));

        // Calculate total repayment
        BigDecimal totalRepayment = deviceAmount.add(interest);

        return totalRepayment.doubleValue();
    }
}