package demo.contract.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceRequest {

    @NotNull
    private BigDecimal deviceAmount;

    @NotNull
    @Size(min = 1, max = 3)
    private List<Integer> repaymentOptions;
}
