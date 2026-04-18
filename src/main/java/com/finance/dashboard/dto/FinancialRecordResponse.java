package com.finance.dashboard.dto;
import com.finance.dashboard.model.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class FinancialRecordResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private LocalDate date;
    private String notes;
    private Long userId;
    private String ownerUsername;  
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
