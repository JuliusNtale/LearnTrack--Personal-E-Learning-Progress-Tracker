
---

# 📚 LearnTrack – Personal E-Learning Progress Tracker

*A Student-Centric System for Tracking Self-Learning Progress*

---

## 🚀 Project Overview

**LearnTrack** is a minimalist, web-based application designed specifically for students to track their personal learning progress. Unlike traditional Learning Management Systems (LMS), LearnTrack empowers students to self-organize and monitor their learning goals, courses, and materials without any complexity or dependency on instructors.

---

## 🎯 Project Objectives

1. Develop a student-only web platform for tracking progress in online/self-paced learning.
2. Provide tools for self-monitoring like time tracking, progress visualization, and completion rates.
3. Create a personalized hub to organize custom courses, study materials, and goals.
4. Simplify tracking with a clean UI and effective features.
5. Ensure accessibility across all devices (desktop, tablet, mobile).

---

## 🧩 Functional Requirements

### 1. 👤 User Management (Students & Admin)
- **Student Registration & Login**
  - Email verification
  - Secure password storage (hashed)
  - Editable student profile: Name, Picture, Bio

- **Admin Dashboard**
  - View, ban, or manage student accounts
  - Monitor system performance
  - Backup/restore data (no course interference)

---

### 2. 📚 Course & Learning Management
- **Personal Course Creation**
  - Add, edit, or delete custom self-learning courses
  - Categorize courses (e.g., Programming, Math, Languages)
  - Define custom completion criteria

- **Learning Materials Upload**
  - Attach PDFs, embed video links (e.g., YouTube)
  - Create searchable tags for easy retrieval

---

### 3. 📈 Progress Tracking & Self-Monitoring
- **Manual Tracking**
  - Mark lessons as completed ✅
  - Add notes or thoughts per module

- **Automated Tracking**
  - Session timer for time logging
  - Visual progress bars showing % completion

- **Achievements & Certificates**
  - Auto-generated certificates upon completion
  - Earn badges for key milestones (e.g., "10 hours studied!")

---

### 4. 📊 Analytics & Reports
- **Student Dashboard**
  - Weekly/monthly study time graphs
  - Completion rate summaries

- **Downloadable Reports**
  - Export personal progress as PDF or CSV

---

### 5. 🔔 Notifications & Reminders
- **Email Alerts**
  - "You haven’t studied [Course] in 3 days!"
  - "Congrats! You completed [Course]!"

- **In-App Reminders**
  - Set custom deadlines and reminders

---

## 🛠️ Tech Stack
| **Area**       | **Technology**              |
|----------------|----------------------------|
| Frontend       | React.js + TypeScript       |
| Backend        | Java Spring Boot            |
| Database       | PostgreSQL                  |
| Authentication | JWT + Spring Security       |
| Deployment     | Heroku (Backend) + Vercel (Frontend) |


---

## ✅ Why This Project Works

- **Simplified Scope** – No instructors; focused only on the student experience.
- **Low Complexity** – Manual tracking, basic analytics — no AI or ML needed.
- **Highly Personal** – Built for individual learners, not institutions.
- **Scalable** – Future integration of AI (recommendations, smart alerts) is possible.

---

## 📂 Folder Structure Suggestion (Coming Soon in Repo)

```bash
learntrack/
├── backend/                  # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/learntrack/
│   │   │   │   ├── config/          # Security, CORS, etc.
│   │   │   │   ├── controller/      # REST APIs
│   │   │   │   ├── dto/             # Request/Response objects
│   │   │   │   ├── exception/       # Custom error handling
│   │   │   │   ├── model/           # JPA Entities
│   │   │   │   ├── repository/      # JPA Repositories
│   │   │   │   ├── service/         # Business logic
│   │   │   │   ├── util/            # Helpers (PDF gen, email, etc.)
│   │   │   │   └── LearnTrackApplication.java
│   │   │   └── resources/
│   │   │       ├── static/          # Certificates/badges templates
│   │   │       ├── templates/       # Email templates (optional)
│   │   │       └── application.properties
│   │   └── test/                    # Unit tests
│   ├── pom.xml                     # Maven dependencies
│   └── Dockerfile                  # For containerization (optional)
│
├── frontend/                 # React Frontend
│   ├── public/
│   │   ├── index.html
│   │   └── assets/           # Favicons, default images
│   ├── src/
│   │   ├── api/              # Axios API calls
│   │   ├── components/       # Reusable UI components
│   │   ├── contexts/         # Auth/Theme contexts
│   │   ├── pages/            # Main views
│   │   ├── styles/           # Global CSS
│   │   ├── types/            # TypeScript interfaces
│   │   ├── utils/            # Helpers (date formatting, etc.)
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   ├── tsconfig.json
│   └── Dockerfile            # For containerization (optional)
│
├── docs/                     # Project documentation
│   ├── ER-Diagram.png        # Database schema
│   ├── Architecture.png      # System design
│   └── Proposal.pdf          # Original project plan
│
├── .gitignore                # Combined ignore rules
├── README.md                 # Project overview (see below)
└── LICENSE                   # MIT License

```
---

## 🚀 Quick Start
### Prerequisites
- Java 17, Maven, Node.js 16+, PostgreSQL

### Run Backend
```bash
cd backend
mvn spring-boot:run
```
---


## 📬 Contributions & Feedback

Want to contribute or suggest features? Open an issue or submit a pull request. Feedback is always welcome!

---

## 🧠 License

This project is for academic purposes under MIT License unless otherwise specified.

---



