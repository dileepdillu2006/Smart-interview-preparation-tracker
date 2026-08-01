package com.dileep.interview_tracker.controller;

import com.dileep.interview_tracker.entity.Problem;
import com.dileep.interview_tracker.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // companyId is optional — pass it as a query param for company-specific practice,
    // omit it for general DSA grinding
    @PostMapping("/user/{userId}")
    public ResponseEntity<Problem> createProblem(
            @PathVariable Long userId,
            @RequestParam(required = false) Long companyId,
            @RequestBody Problem problem) {
        return ResponseEntity.ok(problemService.createProblem(userId, companyId, problem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblem(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Problem>> getProblemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(problemService.getProblemsByUser(userId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Problem>> getProblemsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(problemService.getProblemsByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody Problem problem) {
        return ResponseEntity.ok(problemService.updateProblem(id, problem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}