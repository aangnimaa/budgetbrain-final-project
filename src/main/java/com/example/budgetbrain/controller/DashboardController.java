package com.example.budgetbrain.controller;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.RecordType;
import com.example.budgetbrain.repository.BudgetRepository;
import com.example.budgetbrain.repository.FinanceRecordRepository;
import com.example.budgetbrain.repository.SavingsGoalRepository;
import com.example.budgetbrain.service.CurrentUserService;
import java.math.BigDecimal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final FinanceRecordRepository recordRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public DashboardController(FinanceRecordRepository recordRepository,
                               BudgetRepository budgetRepository,
                               SavingsGoalRepository goalRepository,
                               CurrentUserService currentUserService) {
        this.recordRepository = recordRepository;
        this.budgetRepository = budgetRepository;
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AppUser owner = currentUserService.getUser(userDetails);
        BigDecimal income = valueOrZero(recordRepository.totalByOwnerAndType(owner, RecordType.INCOME));
        BigDecimal expenses = valueOrZero(recordRepository.totalByOwnerAndType(owner, RecordType.EXPENSE));
        BigDecimal balance = income.subtract(expenses);

        model.addAttribute("fullName", owner.getFullName());
        model.addAttribute("income", income);
        model.addAttribute("expenses", expenses);
        model.addAttribute("balance", balance);
        model.addAttribute("totalBudget", valueOrZero(budgetRepository.totalBudgetByOwner(owner)));
        model.addAttribute("totalSaved", valueOrZero(goalRepository.totalSavedByOwner(owner)));
        model.addAttribute("recordCount", recordRepository.countByOwner(owner));
        model.addAttribute("recentRecords", recordRepository.findByOwnerOrderByRecordDateDescCreatedAtDesc(owner).stream().limit(5).toList());
        model.addAttribute("goals", goalRepository.findByOwnerOrderByCreatedAtDesc(owner).stream().limit(3).toList());
        return "dashboard";
    }

    private BigDecimal valueOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
