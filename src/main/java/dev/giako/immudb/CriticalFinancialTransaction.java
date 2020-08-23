package dev.giako.immudb;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriticalFinancialTransaction {
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String beneficiary;
}
