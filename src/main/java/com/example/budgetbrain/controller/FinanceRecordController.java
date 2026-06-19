package com.example.budgetbrain.controller;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.FinanceRecord;
import com.example.budgetbrain.model.RecordType;
import com.example.budgetbrain.repository.FinanceRecordRepository;
import com.example.budgetbrain.service.CurrentUserService;
import com.example.budgetbrain.service.FileStorageService;
import jakarta.validation.Valid;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FinanceRecordController {
    private final FinanceRecordRepository recordRepository;
    private final CurrentUserService currentUserService;
    private final FileStorageService fileStorageService;

    public FinanceRecordController(FinanceRecordRepository recordRepository,
                                   CurrentUserService currentUserService,
                                   FileStorageService fileStorageService) {
        this.recordRepository = recordRepository;
        this.currentUserService = currentUserService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/records")
    public String records(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          Model model) {
        AppUser owner = currentUserService.getUser(userDetails);
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("records", recordRepository.searchByOwner(owner, keyword.trim()));
            model.addAttribute("keyword", keyword.trim());
        } else {
            model.addAttribute("records", recordRepository.findByOwnerOrderByRecordDateDescCreatedAtDesc(owner));
            model.addAttribute("keyword", "");
        }
        return "records";
    }

    @GetMapping("/records/new")
    public String createForm(Model model) {
        model.addAttribute("record", new FinanceRecord());
        model.addAttribute("types", RecordType.values());
        model.addAttribute("mode", "create");
        model.addAttribute("formAction", "/records/new");
        return "record-form";
    }

    @PostMapping("/records/new")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @ModelAttribute("record") FinanceRecord record,
                         BindingResult result,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Model model,
                         RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("types", RecordType.values());
            model.addAttribute("mode", "create");
            model.addAttribute("formAction", "/records/new");
            return "record-form";
        }
        record.setOwner(currentUserService.getUser(userDetails));
        String savedPath = fileStorageService.save(imageFile);
        if (savedPath != null) {
            record.setReceiptImagePath(savedPath);
        }
        recordRepository.save(record);
        redirectAttributes.addFlashAttribute("success", "Finance record created successfully.");
        return "redirect:/records";
    }

    @GetMapping("/records/{id}")
    public String detail(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        FinanceRecord record = getOwnedRecord(userDetails, id);
        model.addAttribute("record", record);
        return "record-detail";
    }

    @GetMapping("/records/{id}/edit")
    public String editForm(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        FinanceRecord record = getOwnedRecord(userDetails, id);
        model.addAttribute("record", record);
        model.addAttribute("types", RecordType.values());
        model.addAttribute("mode", "edit");
        model.addAttribute("formAction", "/records/" + id + "/edit");
        return "record-form";
    }

    @PostMapping("/records/{id}/edit")
    public String update(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("record") FinanceRecord formRecord,
                         BindingResult result,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Model model,
                         RedirectAttributes redirectAttributes) throws IOException {
        FinanceRecord existing = getOwnedRecord(userDetails, id);
        if (result.hasErrors()) {
            formRecord.setReceiptImagePath(existing.getReceiptImagePath());
            model.addAttribute("types", RecordType.values());
            model.addAttribute("mode", "edit");
            model.addAttribute("formAction", "/records/" + id + "/edit");
            return "record-form";
        }

        existing.setTitle(formRecord.getTitle());
        existing.setType(formRecord.getType());
        existing.setCategory(formRecord.getCategory());
        existing.setAmount(formRecord.getAmount());
        existing.setRecordDate(formRecord.getRecordDate());
        existing.setNote(formRecord.getNote());
        String savedPath = fileStorageService.save(imageFile);
        if (savedPath != null) {
            existing.setReceiptImagePath(savedPath);
        }
        recordRepository.save(existing);
        redirectAttributes.addFlashAttribute("success", "Finance record updated successfully.");
        return "redirect:/records";
    }

    @PostMapping("/records/{id}/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        FinanceRecord record = getOwnedRecord(userDetails, id);
        recordRepository.delete(record);
        redirectAttributes.addFlashAttribute("success", "Finance record deleted successfully.");
        return "redirect:/records";
    }

    private FinanceRecord getOwnedRecord(UserDetails userDetails, Long id) {
        AppUser owner = currentUserService.getUser(userDetails);
        FinanceRecord record = recordRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Finance record not found"));
        if (!record.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("You do not have permission to access this record");
        }
        return record;
    }
}
