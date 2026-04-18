package com.finance.dashboard.controller;
import com.finance.dashboard.dto.FinancialRecordRequest;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.TransactionType;
import com.finance.dashboard.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {
    private final FinancialRecordService recordService;
    public FinancialRecordController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }
    @PostMapping
    public ResponseEntity<FinancialRecordResponse> createRecord(
            @Valid @RequestBody FinancialRecordRequest request,
            @RequestHeader("X-User-Role") Role requesterRole) {
        FinancialRecordResponse created = recordService.createRecord(request, requesterRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> getRecordById(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") Role requesterRole) {
        FinancialRecordResponse record = recordService.getRecordById(id, requesterRole);
        return ResponseEntity.ok(record);
    }
    @GetMapping
    public ResponseEntity<List<FinancialRecordResponse>> getRecords(
            @RequestParam Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("X-User-Role") Role requesterRole) {
        List<FinancialRecordResponse> records = recordService.getRecords(
                userId, type, category, startDate, endDate, requesterRole);
        return ResponseEntity.ok(records);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecordRequest request,
            @RequestHeader("X-User-Role") Role requesterRole) {
        FinancialRecordResponse updated = recordService.updateRecord(id, request, requesterRole);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") Role requesterRole) {
        recordService.deleteRecord(id, requesterRole);
        return ResponseEntity.noContent().build();
    }
}
