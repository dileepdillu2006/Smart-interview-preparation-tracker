package com.dileep.interview_tracker.service;

import com.dileep.interview_tracker.entity.ApplicationStage;
import com.dileep.interview_tracker.entity.Company;
import com.dileep.interview_tracker.entity.User;
import com.dileep.interview_tracker.repository.ApplicationStageRepository;
import com.dileep.interview_tracker.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationStageRepository applicationStageRepository;

    @Autowired
    private UserService userService;

    public Company createCompany(Long userId, Company company) {
        User user = userService.getUserById(userId); // reuse — throws if user doesn't exist
        company.setUser(user);
        Company savedCompany = companyRepository.save(company);

        // Automatically log the first stage so every company has a history from day one
        ApplicationStage initialStage = new ApplicationStage(
                ApplicationStage.Stage.APPLIED,
                LocalDate.now(),
                "Application created",
                savedCompany
        );
        applicationStageRepository.save(initialStage);

        return savedCompany;
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    public List<Company> getCompaniesByUser(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company existing = getCompanyById(id);
        existing.setName(updatedCompany.getName());
        existing.setRole(updatedCompany.getRole());
        existing.setPriority(updatedCompany.getPriority());
        existing.setApplicationDeadline(updatedCompany.getApplicationDeadline());
        existing.setNotes(updatedCompany.getNotes());
        return companyRepository.save(existing);
    }

    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    // Convenience method: get the current stage of a company (most recent one)
    public ApplicationStage getCurrentStage(Long companyId) {
       List<ApplicationStage> stages = applicationStageRepository
        .findByCompanyIdOrderByDateChangedDescIdDesc(companyId);
        if (stages.isEmpty()) {
            throw new RuntimeException("No stages found for company id: " + companyId);
        }
        return stages.get(0);
    }

    // Move a company to a new stage — this is the core "progress tracker" action
    public ApplicationStage advanceStage(Long companyId, ApplicationStage.Stage newStage, String notes) {
        Company company = getCompanyById(companyId); // validates company exists
        ApplicationStage stage = new ApplicationStage(newStage, LocalDate.now(), notes, company);
        return applicationStageRepository.save(stage);
    }
}