package com.finance.dashboard.repository;
import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    List<FinancialRecord> findByUserId(Long userId);
    List<FinancialRecord> findByUserIdAndType(Long userId, TransactionType type);
    List<FinancialRecord> findByUserIdAndCategory(Long userId, String category);
    List<FinancialRecord> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<FinancialRecord> findByUserIdAndTypeAndCategory(Long userId, TransactionType type, String category);
    List<FinancialRecord> findByUserIdAndCategoryAndDateBetween(
            Long userId, String category, LocalDate startDate, LocalDate endDate);
    List<FinancialRecord> findTop5ByUserIdOrderByDateDesc(Long userId);
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f " +
           "WHERE f.user.id = :userId AND f.type = 'INCOME'")
    BigDecimal getTotalIncomeByUserId(@Param("userId") Long userId);
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f " +
           "WHERE f.user.id = :userId AND f.type = 'EXPENSE'")
    BigDecimal getTotalExpenseByUserId(@Param("userId") Long userId);
    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f " +
           "WHERE f.user.id = :userId AND f.type = :type " +
           "GROUP BY f.category")
    List<Object[]> getCategoryWiseTotals(@Param("userId") Long userId,
                                          @Param("type") TransactionType type);
    boolean existsByIdAndUserId(Long recordId, Long userId);
    List<FinancialRecord> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
}
