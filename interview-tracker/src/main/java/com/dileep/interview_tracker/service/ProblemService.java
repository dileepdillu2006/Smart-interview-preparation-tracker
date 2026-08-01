package com.dileep.interview_tracker.service;

import com.dileep.interview_tracker.entity.Problem;
import com.dileep.interview_tracker.entity.User;
import com.dileep.interview_tracker.entity.Company;
import com.dileep.interview_tracker.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public Problem createProblem(Long userId, Long companyId, Problem problem) {
        User user = userService.getUserById(userId); // throws if invalid
        problem.setUser(user);

        // companyId is optional — general practice problems aren't tied to any company
        if (companyId != null) {
            Company company = companyService.getCompanyById(companyId); // throws if invalid
            problem.setCompany(company);
        }

        return problemRepository.save(problem);
    }

    public Problem getProblemById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }

    public List<Problem> getProblemsByUser(Long userId) {
        return problemRepository.findByUserId(userId);
    }

    public List<Problem> getProblemsByCompany(Long companyId) {
        return problemRepository.findByCompanyId(companyId);
    }

    public Problem updateProblem(Long id, Problem updatedProblem) {
        Problem existing = getProblemById(id);
        existing.setTitle(updatedProblem.getTitle());
        existing.setDifficulty(updatedProblem.getDifficulty());
        existing.setTopic(updatedProblem.getTopic());
        existing.setStatus(updatedProblem.getStatus());
        existing.setDatePracticed(updatedProblem.getDatePracticed());
        return problemRepository.save(existing);
    }

    public void deleteProblem(Long id) {
        Problem problem = getProblemById(id);
        problemRepository.delete(problem);
    }
}