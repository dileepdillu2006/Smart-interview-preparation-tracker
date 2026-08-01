package com.dileep.interview_tracker.controller;

import com.dileep.interview_tracker.entity.MockInterview;
import com.dileep.interview_tracker.service.MockInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock-interviews")
public class MockInterviewController {

    @Autowired
    private MockInterviewService mockInterviewService;

    @PostMapping("/user/{userId}/company/{companyId}")
    public ResponseEntity<MockInterview> createMockInterview(
            @PathVariable Long userId,
            @PathVariable Long companyId,
            @RequestBody MockInterview mockInterview) {
        return ResponseEntity.ok(mockInterviewService.createMockInterview(userId, companyId, mockInterview));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MockInterview> getMockInterview(@PathVariable Long id) {
        return ResponseEntity.ok(mockInterviewService.getMockInterviewById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<MockInterview>> getMockInterviewsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(mockInterviewService.getMockInterviewsByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MockInterview> updateMockInterview(@PathVariable Long id, @RequestBody MockInterview mockInterview) {
        return ResponseEntity.ok(mockInterviewService.updateMockInterview(id, mockInterview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMockInterview(@PathVariable Long id) {
        mockInterviewService.deleteMockInterview(id);
        return ResponseEntity.noContent().build();
    }
}