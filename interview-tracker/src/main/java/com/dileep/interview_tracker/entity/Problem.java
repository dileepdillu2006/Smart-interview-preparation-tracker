package com.dileep.interview_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String topic;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate datePracticed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    @JsonIgnore
    private Company company;

    public enum Difficulty { EASY, MEDIUM, HARD }
    public enum Status { SOLVED, UNSOLVED, REVISIT }

    public Problem() {}

    public Problem(String title, Difficulty difficulty, String topic, Status status,
                   LocalDate datePracticed, User user, Company company) {
        this.title = title;
        this.difficulty = difficulty;
        this.topic = topic;
        this.status = status;
        this.datePracticed = datePracticed;
        this.user = user;
        this.company = company;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getDatePracticed() { return datePracticed; }
    public void setDatePracticed(LocalDate datePracticed) { this.datePracticed = datePracticed; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}