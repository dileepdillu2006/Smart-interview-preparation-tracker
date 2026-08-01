# Smart Interview Preparation Tracker

A full-stack web application that helps students preparing for campus placements track their interview preparation across multiple companies — application stages, coding problems practiced, and AI-powered mock interviews tailored to each role.

## Features

- **JWT Authentication** — secure user registration and login with encrypted passwords
- **Company-wise Progress Tracking** — track application stages (Applied → OA → Interview Rounds → Offer/Rejected) with full history per company
- **Problem Tracker** — log coding problems practiced per company, with difficulty and topic tags
- **AI-Generated Practice Questions** — Gemini AI generates coding and conceptual questions tailored to each company's job description
- **AI Mock Interview** — interactive chat-based mock interview using Gemini AI, personalized with the candidate's resume and the company's job description

## Tech Stack

**Backend:** Java, Spring Boot, Spring Security, Spring Data JPA, MySQL, JWT, Google Gemini API
**Frontend:** React, React Router, Vite

## Architecture

- REST API with 5 core entities: User, Company, ApplicationStage, Problem, MockInterview
- Stage-history design (not a single status flag) — every stage transition is logged with a timestamp, giving a full timeline per company
- JWT-based stateless authentication with BCrypt password hashing
- AI service layer abstracts Gemini API calls, keeping prompt logic separate from controllers

## Running Locally

### Backend
1. Set up a MySQL database named `interview_tracker`
2. Configure `application.properties` with your DB credentials and Gemini API key
3. Run: `./mvnw spring-boot:run`

### Frontend
1. `npm install`
2. `npm run dev`
