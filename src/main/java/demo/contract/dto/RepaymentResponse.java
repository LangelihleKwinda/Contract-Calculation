package demo.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepaymentResponse {

    private double deviceAmount;
    private int repaymentPeriod;
    private double interestRate;
    private double totalRepayment;

}
