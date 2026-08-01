package com.dileep.interview_tracker.controller;

import com.dileep.interview_tracker.entity.ApplicationStage;
import com.dileep.interview_tracker.entity.Company;
import com.dileep.interview_tracker.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Create a company for a given user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Company> createCompany(@PathVariable Long userId, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(userId, company));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    // Get all companies for a given user — your "dashboard" endpoint
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Company>> getCompaniesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(companyService.getCompaniesByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(id, company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // The core progress-tracker endpoints:

    @GetMapping("/{id}/current-stage")
    public ResponseEntity<ApplicationStage> getCurrentStage(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCurrentStage(id));
    }

    @PostMapping("/{id}/advance-stage")
    public ResponseEntity<ApplicationStage> advanceStage(
            @PathVariable Long id,
            @RequestParam ApplicationStage.Stage stage,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(companyService.advanceStage(id, stage, notes));
    }
}