package com.dileep.interview_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Column(columnDefinition = "TEXT")
private String jobDescription;
    @Column(nullable = false)
    private String name;

    private String role;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate applicationDeadline;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<ApplicationStage> applicationStages;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Problem> problems;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<MockInterview> mockInterviews;
@Column(columnDefinition = "TEXT")
private String generatedQuestions;
    public enum Priority { HIGH, MEDIUM, LOW }

    public Company() {}
public String getJobDescription() { return jobDescription; }
public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public Company(String name, String role, Priority priority, LocalDate applicationDeadline, User user) {
        this.name = name;
        this.role = role;
        this.priority = priority;
        this.applicationDeadline = applicationDeadline;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
public String getGeneratedQuestions() { return generatedQuestions; }
public void setGeneratedQuestions(String generatedQuestions) { this.generatedQuestions = generatedQuestions; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<ApplicationStage> getApplicationStages() { return applicationStages; }
    public void setApplicationStages(List<ApplicationStage> applicationStages) { this.applicationStages = applicationStages; }

    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }

    public List<MockInterview> getMockInterviews() { return mockInterviews; }
    public void setMockInterviews(List<MockInterview> mockInterviews) { this.mockInterviews = mockInterviews; }
}