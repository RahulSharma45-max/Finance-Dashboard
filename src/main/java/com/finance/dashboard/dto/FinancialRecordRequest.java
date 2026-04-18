package com.finance.dashboard.dto;
import com.finance.dashboard.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class FinancialRecordRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private TransactionType type;
    @NotBlank(message = "Category is required")
    private String category;
    @NotNull(message = "Date is required")
    private LocalDate date;
    private String notes;
}
