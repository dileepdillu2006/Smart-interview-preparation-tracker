package com.dileep.interview_tracker.controller;

import com.dileep.interview_tracker.ai.GeminiService;
import com.dileep.interview_tracker.dto.*;
import com.dileep.interview_tracker.entity.Company;
import com.dileep.interview_tracker.entity.User;
import com.dileep.interview_tracker.service.CompanyService;
import com.dileep.interview_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // Starts a mock interview: generates the first question based on JD + resume
    @PostMapping("/interview/start/{companyId}/user/{userId}")
    public ResponseEntity<InterviewChatResponse> startInterview(
            @PathVariable Long companyId, @PathVariable Long userId) {

        Company company = companyService.getCompanyById(companyId);
        User user = userService.getUserById(userId);

        String jd = company.getJobDescription() != null ? company.getJobDescription() : "General software engineering role";
        String resume = user.getResumeText() != null ? user.getResumeText() : "No resume provided";

        String prompt = """
                You are a professional technical interviewer conducting a mock interview for the role: %s at %s.

                Job description: %s

                Candidate resume: %s

                Ask exactly ONE relevant interview question to start (mix of technical and behavioral is fine).
                Keep it concise, 1-3 sentences. Do not include any preamble like "Sure!" or "Question 1:" — just ask the question directly.
                """.formatted(company.getRole(), company.getName(), jd, resume);

        String question = geminiService.generateContent(prompt);
        return ResponseEntity.ok(new InterviewChatResponse(question));
    }

    // Continues the interview: candidate answered, get feedback + next question
    @PostMapping("/interview/next")
    public ResponseEntity<InterviewChatResponse> nextQuestion(@RequestBody InterviewChatRequest request) {
        Company company = companyService.getCompanyById(request.getCompanyId());

        StringBuilder historyText = new StringBuilder();
        for (ChatMessageDto msg : request.getHistory()) {
            historyText.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }

        String prompt = """
                You are a professional technical interviewer for the role: %s at %s.
                Job description: %s

                Here is the conversation so far:
                %s

                Give brief feedback (1-2 sentences) on the candidate's last answer, then ask the next relevant question.
                Keep the whole response under 4 sentences total. Be direct and professional, no filler.
                """.formatted(company.getRole(), company.getName(),
                company.getJobDescription() != null ? company.getJobDescription() : "General role",
                historyText.toString());

        String reply = geminiService.generateContent(prompt);
        return ResponseEntity.ok(new InterviewChatResponse(reply));
    }

    // Generates a set of coding + conceptual questions based on the JD
    @PostMapping("/questions/{companyId}")
public ResponseEntity<InterviewChatResponse> generateQuestions(@PathVariable Long companyId) {
    Company company = companyService.getCompanyById(companyId);
    String jd = company.getJobDescription() != null ? company.getJobDescription() : "General software engineering role";

    String prompt = """
            Based on this job description for a %s role at %s:
            %s

            Generate a list of exactly 5 coding practice questions and 3 conceptual questions
            that would help someone prepare for this specific role.
            Format as a numbered list, coding questions first, then conceptual questions.
            Keep each item to one line.
            """.formatted(company.getRole(), company.getName(), jd);

    String questions = geminiService.generateContent(prompt);
    company.setGeneratedQuestions(questions);
    companyService.updateCompany(companyId, company);
    return ResponseEntity.ok(new InterviewChatResponse(questions));
}
}