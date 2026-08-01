package com.dileep.interview_tracker.dto;

public class InterviewChatResponse {
    private String reply;

    public InterviewChatResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}