package com.finance.dashboard.service;
import com.finance.dashboard.dto.DashboardResponse;
import com.finance.dashboard.dto.FinancialRecordRequest;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.exception.AccessDeniedException;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.TransactionType;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class FinancialRecordService {
    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;
    public FinancialRecordService(FinancialRecordRepository recordRepository,
                                   UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }
    public FinancialRecordResponse createRecord(FinancialRecordRequest request, Role requesterRole) {
        if (requesterRole == Role.VIEWER) {
            throw new AccessDeniedException("VIEWER role does not have permission to create records.");
        }
        User user = findUserOrThrow(request.getUserId());
        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory().trim()); 
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        record.setUser(user); 
        FinancialRecord saved = recordRepository.save(record);
        return convertToResponse(saved);
    }
    public FinancialRecordResponse getRecordById(Long recordId, Role requesterRole) {
        FinancialRecord record = findRecordOrThrow(recordId);
        return convertToResponse(record);
    }
    public List<FinancialRecordResponse> getRecords(Long userId,
                                                     TransactionType type,
                                                     String category,
                                                     LocalDate startDate,
                                                     LocalDate endDate,
                                                     Role requesterRole) {
        findUserOrThrow(userId);
        List<FinancialRecord> records;
        boolean hasType = type != null;
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasDateRange = startDate != null && endDate != null;
        if (hasType && hasCategory) {
            records = recordRepository.findByUserIdAndTypeAndCategory(userId, type, category);
        } else if (hasCategory && hasDateRange) {
            records = recordRepository.findByUserIdAndCategoryAndDateBetween(
                    userId, category, startDate, endDate);
        } else if (hasType) {
            records = recordRepository.findByUserIdAndType(userId, type);
        } else if (hasCategory) {
            records = recordRepository.findByUserIdAndCategory(userId, category);
        } else if (hasDateRange) {
            records = recordRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        } else {
            records = recordRepository.findByUserId(userId);
        }
        return records.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public FinancialRecordResponse updateRecord(Long recordId,
                                                FinancialRecordRequest request,
                                                Role requesterRole) {
        if (requesterRole == Role.VIEWER) {
            throw new AccessDeniedException("VIEWER role does not have permission to update records.");
        }
        FinancialRecord existing = findRecordOrThrow(recordId);
        existing.setAmount(request.getAmount());
        existing.setType(request.getType());
        existing.setCategory(request.getCategory().trim());
        existing.setDate(request.getDate());
        existing.setNotes(request.getNotes());
        if (!existing.getUser().getId().equals(request.getUserId())) {
            User newOwner = findUserOrThrow(request.getUserId());
            existing.setUser(newOwner);
        }
        FinancialRecord updated = recordRepository.save(existing);
        return convertToResponse(updated);
    }
    public void deleteRecord(Long recordId, Role requesterRole) {
        if (requesterRole != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN can delete financial records.");
        }
        FinancialRecord record = findRecordOrThrow(recordId);
        recordRepository.delete(record);
    }
    public DashboardResponse getDashboard(Long userId, Role requesterRole) {
        if (requesterRole == Role.VIEWER) {
            throw new AccessDeniedException("VIEWER role cannot access dashboard analytics.");
        }
        findUserOrThrow(userId);
        BigDecimal totalIncome = recordRepository.getTotalIncomeByUserId(userId);
        BigDecimal totalExpenses = recordRepository.getTotalExpenseByUserId(userId);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        Map<String, BigDecimal> incomeCategoryTotals = buildCategoryMap(
                recordRepository.getCategoryWiseTotals(userId, TransactionType.INCOME)
        );
        Map<String, BigDecimal> expenseCategoryTotals = buildCategoryMap(
                recordRepository.getCategoryWiseTotals(userId, TransactionType.EXPENSE)
        );
        List<FinancialRecordResponse> recentTransactions =
                recordRepository.findTop5ByUserIdOrderByDateDesc(userId)
                        .stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
        return new DashboardResponse(
                totalIncome,
                totalExpenses,
                netBalance,
                incomeCategoryTotals,
                expenseCategoryTotals,
                recentTransactions
        );
    }
    private Map<String, BigDecimal> buildCategoryMap(List<Object[]> rawResults) {
        Map<String, BigDecimal> categoryMap = new HashMap<>();
        for (Object[] row : rawResults) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            categoryMap.put(category, total);
        }
        return categoryMap;
    }
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User with ID " + userId + " not found."
                ));
    }
    private FinancialRecord findRecordOrThrow(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Financial record with ID " + recordId + " not found."
                ));
    }
    public FinancialRecordResponse convertToResponse(FinancialRecord record) {
        FinancialRecordResponse response = new FinancialRecordResponse();
        response.setId(record.getId());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setCategory(record.getCategory());
        response.setDate(record.getDate());
        response.setNotes(record.getNotes());
        response.setUserId(record.getUser().getId());
        response.setOwnerUsername(record.getUser().getUsername());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
