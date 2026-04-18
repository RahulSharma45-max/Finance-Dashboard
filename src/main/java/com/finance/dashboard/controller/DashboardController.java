package com.finance.dashboard.controller;
import com.finance.dashboard.dto.DashboardResponse;
import com.finance.dashboard.model.Role;
import com.finance.dashboard.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final FinancialRecordService recordService;
    public DashboardController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }
    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getDashboardSummary(
            @RequestParam Long userId,
            @RequestHeader("X-User-Role") Role requesterRole) {
        DashboardResponse summary = recordService.getDashboard(userId, requesterRole);
        return ResponseEntity.ok(summary);
    }
}
