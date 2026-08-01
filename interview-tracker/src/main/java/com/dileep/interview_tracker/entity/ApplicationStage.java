package com.dileep.interview_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "application_stages")
public class ApplicationStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage stage;

    @Column(nullable = false)
    private LocalDate dateChanged;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    public enum Stage {
        APPLIED, OA_SCHEDULED, OA_COMPLETED,
        INTERVIEW_ROUND_1, INTERVIEW_ROUND_2, INTERVIEW_ROUND_3,
        HR_ROUND, OFFER, REJECTED
    }

    public ApplicationStage() {}

    public ApplicationStage(Stage stage, LocalDate dateChanged, String notes, Company company) {
        this.stage = stage;
        this.dateChanged = dateChanged;
        this.notes = notes;
        this.company = company;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Stage getStage() { return stage; }
    public void setStage(Stage stage) { this.stage = stage; }

    public LocalDate getDateChanged() { return dateChanged; }
    public void setDateChanged(LocalDate dateChanged) { this.dateChanged = dateChanged; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}