package com.example.budgetbrain.controller;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.Budget;
import com.example.budgetbrain.repository.BudgetRepository;
import com.example.budgetbrain.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BudgetController {
    private final BudgetRepository budgetRepository;
    private final CurrentUserService currentUserService;

    public BudgetController(BudgetRepository budgetRepository, CurrentUserService currentUserService) {
        this.budgetRepository = budgetRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/budgets")
    public String budgets(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          Model model) {
        AppUser owner = currentUserService.getUser(userDetails);
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("budgets", budgetRepository.searchByOwner(owner, keyword.trim()));
            model.addAttribute("keyword", keyword.trim());
        } else {
            model.addAttribute("budgets", budgetRepository.findByOwnerOrderByCreatedAtDesc(owner));
            model.addAttribute("keyword", "");
        }
        return "budgets";
    }

    @GetMapping("/budgets/new")
    public String createForm(Model model) {
        model.addAttribute("budget", new Budget());
        model.addAttribute("mode", "create");
        model.addAttribute("formAction", "/budgets/new");
        return "budget-form";
    }

    @PostMapping("/budgets/new")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @ModelAttribute("budget") Budget budget,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("formAction", "/budgets/new");
            return "budget-form";
        }
        budget.setOwner(currentUserService.getUser(userDetails));
        budgetRepository.save(budget);
        redirectAttributes.addFlashAttribute("success", "Budget created successfully.");
        return "redirect:/budgets";
    }

    @GetMapping("/budgets/{id}/edit")
    public String editForm(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        Budget budget = getOwnedBudget(userDetails, id);
        model.addAttribute("budget", budget);
        model.addAttribute("mode", "edit");
        model.addAttribute("formAction", "/budgets/" + id + "/edit");
        return "budget-form";
    }

    @PostMapping("/budgets/{id}/edit")
    public String update(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("budget") Budget formBudget,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        Budget existing = getOwnedBudget(userDetails, id);
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("formAction", "/budgets/" + id + "/edit");
            return "budget-form";
        }
        existing.setCategory(formBudget.getCategory());
        existing.setMonth(formBudget.getMonth());
        existing.setLimitAmount(formBudget.getLimitAmount());
        existing.setNote(formBudget.getNote());
        budgetRepository.save(existing);
        redirectAttributes.addFlashAttribute("success", "Budget updated successfully.");
        return "redirect:/budgets";
    }

    @PostMapping("/budgets/{id}/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        Budget budget = getOwnedBudget(userDetails, id);
        budgetRepository.delete(budget);
        redirectAttributes.addFlashAttribute("success", "Budget deleted successfully.");
        return "redirect:/budgets";
    }

    private Budget getOwnedBudget(UserDetails userDetails, Long id) {
        AppUser owner = currentUserService.getUser(userDetails);
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Budget not found"));
        if (!budget.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("You do not have permission to access this budget");
        }
        return budget;
    }
}
