package demo.contract.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepaymentOffer {
    private int repaymentPeriod;
    private BigDecimal repaymentAmount;

}
