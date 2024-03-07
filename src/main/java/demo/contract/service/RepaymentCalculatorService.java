package demo.contract.service;

import demo.contract.dto.RepaymentOffer;
import demo.contract.exception.CustomExceptionHandler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface RepaymentCalculatorService {
    List<RepaymentOffer> calculateRepayments(BigDecimal deviceRequest) throws CustomExceptionHandler;
}
