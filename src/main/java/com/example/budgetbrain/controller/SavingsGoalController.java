package com.example.budgetbrain.controller;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.SavingsGoal;
import com.example.budgetbrain.repository.SavingsGoalRepository;
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
public class SavingsGoalController {
    private final SavingsGoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public SavingsGoalController(SavingsGoalRepository goalRepository, CurrentUserService currentUserService) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/goals")
    public String goals(@AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {
        AppUser owner = currentUserService.getUser(userDetails);
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("goals", goalRepository.searchByOwner(owner, keyword.trim()));
            model.addAttribute("keyword", keyword.trim());
        } else {
            model.addAttribute("goals", goalRepository.findByOwnerOrderByCreatedAtDesc(owner));
            model.addAttribute("keyword", "");
        }
        return "goals";
    }

    @GetMapping("/goals/new")
    public String createForm(Model model) {
        model.addAttribute("goal", new SavingsGoal());
        model.addAttribute("mode", "create");
        model.addAttribute("formAction", "/goals/new");
        return "goal-form";
    }

    @PostMapping("/goals/new")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @ModelAttribute("goal") SavingsGoal goal,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("formAction", "/goals/new");
            return "goal-form";
        }
        goal.setOwner(currentUserService.getUser(userDetails));
        goalRepository.save(goal);
        redirectAttributes.addFlashAttribute("success", "Savings goal created successfully.");
        return "redirect:/goals";
    }

    @GetMapping("/goals/{id}/edit")
    public String editForm(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        SavingsGoal goal = getOwnedGoal(userDetails, id);
        model.addAttribute("goal", goal);
        model.addAttribute("mode", "edit");
        model.addAttribute("formAction", "/goals/" + id + "/edit");
        return "goal-form";
    }

    @PostMapping("/goals/{id}/edit")
    public String update(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("goal") SavingsGoal formGoal,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        SavingsGoal existing = getOwnedGoal(userDetails, id);
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("formAction", "/goals/" + id + "/edit");
            return "goal-form";
        }
        existing.setName(formGoal.getName());
        existing.setTargetAmount(formGoal.getTargetAmount());
        existing.setSavedAmount(formGoal.getSavedAmount());
        existing.setDeadline(formGoal.getDeadline());
        existing.setNote(formGoal.getNote());
        goalRepository.save(existing);
        redirectAttributes.addFlashAttribute("success", "Savings goal updated successfully.");
        return "redirect:/goals";
    }

    @PostMapping("/goals/{id}/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        SavingsGoal goal = getOwnedGoal(userDetails, id);
        goalRepository.delete(goal);
        redirectAttributes.addFlashAttribute("success", "Savings goal deleted successfully.");
        return "redirect:/goals";
    }

    private SavingsGoal getOwnedGoal(UserDetails userDetails, Long id) {
        AppUser owner = currentUserService.getUser(userDetails);
        SavingsGoal goal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Savings goal not found"));
        if (!goal.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("You do not have permission to access this goal");
        }
        return goal;
    }
}
