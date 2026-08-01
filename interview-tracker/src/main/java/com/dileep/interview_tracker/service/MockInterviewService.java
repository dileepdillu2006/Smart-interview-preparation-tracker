package com.dileep.interview_tracker.service;

import com.dileep.interview_tracker.entity.MockInterview;
import com.dileep.interview_tracker.entity.User;
import com.dileep.interview_tracker.entity.Company;
import com.dileep.interview_tracker.repository.MockInterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockInterviewService {

    @Autowired
    private MockInterviewRepository mockInterviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public MockInterview createMockInterview(Long userId, Long companyId, MockInterview mockInterview) {
        User user = userService.getUserById(userId);
        Company company = companyService.getCompanyById(companyId); // required, unlike Problem
        mockInterview.setUser(user);
        mockInterview.setCompany(company);
        return mockInterviewRepository.save(mockInterview);
    }

    public MockInterview getMockInterviewById(Long id) {
        return mockInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mock interview not found with id: " + id));
    }

    public List<MockInterview> getMockInterviewsByCompany(Long companyId) {
        return mockInterviewRepository.findByCompanyId(companyId);
    }

    public MockInterview updateMockInterview(Long id, MockInterview updated) {
        MockInterview existing = getMockInterviewById(id);
        existing.setDate(updated.getDate());
        existing.setRoundType(updated.getRoundType());
        existing.setScore(updated.getScore());
        existing.setFeedback(updated.getFeedback());
        return mockInterviewRepository.save(existing);
    }

    public void deleteMockInterview(Long id) {
        MockInterview mockInterview = getMockInterviewById(id);
        mockInterviewRepository.delete(mockInterview);
    }
}