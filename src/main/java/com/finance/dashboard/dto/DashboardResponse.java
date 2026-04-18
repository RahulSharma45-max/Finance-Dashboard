package com.finance.dashboard.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> incomeCategoryTotals;
    private Map<String, BigDecimal> expenseCategoryTotals;
    private List<FinancialRecordResponse> recentTransactions;
}
