package com.dileep.interview_tracker.dto;

import java.util.List;

public class InterviewChatRequest {
    private Long companyId;
    private List<ChatMessageDto> history; // full conversation so far

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public List<ChatMessageDto> getHistory() { return history; }
    public void setHistory(List<ChatMessageDto> history) { this.history = history; }
}